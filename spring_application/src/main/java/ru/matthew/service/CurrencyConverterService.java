package ru.matthew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.matthew.exception.CurrencyNotFoundException;
import ru.matthew.exception.ServiceUnavailableException;
import ru.matthew.exception.UnsupportedCurrencyException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CurrencyConverterService {
    @Value("${cbr.api.url}")
    private String cbrApiUrl;

    private final RestTemplate restTemplate;

    public CurrencyConverterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> getCurrencyRates() {

        String xmlResponse = fetchCurrencyRatesFromApi();
        log.debug("Получен XML ответ от API: {}", xmlResponse);

        Document xmlDoc = parseXmlResponse(xmlResponse);
        log.debug("XML ответ успешно распарсен");

        Map<String, Double> rates = new HashMap<>();
        NodeList nList = xmlDoc.getElementsByTagName("Valute");
        for (int i = 0; i < nList.getLength(); i++) {
            String currencyCode = nList.item(i).getChildNodes().item(1).getTextContent();
            double rate = Double.parseDouble(nList.item(i).getChildNodes().item(5).
                    getTextContent().replace(",", "."));
            rates.put(currencyCode, rate);
            log.debug("Добавлен курс валюты: {} = {}", currencyCode, rate);
        }

        log.info("Курсы валют успешно получены: {}", rates);
        return rates;
    }

    private String fetchCurrencyRatesFromApi() {
        log.info("Запрос курсов валют из внешнего API: {}", cbrApiUrl);
        try {
            return restTemplate.getForObject(cbrApiUrl, String.class);
        } catch (RestClientException e) {
            log.error("Ошибка при запросе курсов валют: {}", e.getMessage(), e);
            throw new ServiceUnavailableException("Currency service is unavailable", 3600);
        }
    }

    private Document parseXmlResponse(String xmlResponse) {
        log.debug("Начинается парсинг XML ответа");
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return dBuilder.parse(new InputSource(new StringReader(xmlResponse)));
        } catch (Exception e) {
            log.error("Ошибка при парсинге XML: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        validateCurrencyCodes(fromCurrency, toCurrency);

        Map<String, Double> rates = getCurrencyRates();
        log.debug("Полученные курсы валют для конвертации: {}", rates);

        double fromRate = getRate(rates, fromCurrency);
        double toRate = getRate(rates, toCurrency);
        log.debug("Курс для {}: {}, Курс для {}: {}", fromCurrency, fromRate, toCurrency, toRate);

        double convertedAmount = (amount * fromRate) / toRate;
        log.info("Конвертация успешна: {} {} в {} = {}", amount, fromCurrency, toCurrency, convertedAmount);
        return convertedAmount;
    }

    private void validateCurrencyCodes(String... currencyCodes) {
        for (String currencyCode : currencyCodes) {
            if (isValidCurrencyCode(currencyCode)) {
                log.warn("Не поддерживаемый код валюты при валидации: {}", currencyCode);
                throw new UnsupportedCurrencyException("Unsupported currency code: " + currencyCode);
            }
        }
    }

    private double getRate(Map<String, Double> rates, String currency) {
        if ("RUB".equals(currency)) {
            return 1.0;
        }
        Double rate = rates.get(currency);

        if (rate == null || rate.isNaN()) {
            log.error("Курс не найден для кода: {}", currency);
            throw new CurrencyNotFoundException("Currency rate not found for code: " + currency);
        }

        return rate;
    }

    private boolean isValidCurrencyCode(String currencyCode) {
        boolean isValid = Currency.getAvailableCurrencies().stream()
                .noneMatch(currency -> currency.getCurrencyCode().equals(currencyCode));
        log.debug("Проверка кода валюты {}: {}", currencyCode, isValid);
        return isValid;
    }
}
