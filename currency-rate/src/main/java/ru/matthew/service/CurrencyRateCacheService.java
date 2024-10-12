package ru.matthew.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.matthew.exception.ServiceUnavailableException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CurrencyRateCacheService {
    @Value("${cbr.api.url}")
    private String cbrApiUrl;

    private final RestClient restClient;

    public CurrencyRateCacheService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Cacheable(value = "currencyRates", unless = "#result == null")
    @CircuitBreaker(name = "currencyRateService")
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
            return restClient
                    .get()
                    .uri(cbrApiUrl)
                    .header("Content-Type", "application/xml")
                    .retrieve()
                    .body(String.class);
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

    // Метод для сброса кэша
    @Scheduled(fixedRate = 3600000)
    @CacheEvict(value = "currencyRates", allEntries = true)
    public void evictCurrencyRatesCache() {
        log.info("Кэш курсов валют сброшен");
    }
}
