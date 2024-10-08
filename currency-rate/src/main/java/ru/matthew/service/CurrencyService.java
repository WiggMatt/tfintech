package ru.matthew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matthew.dto.CurrencyRateResponse;
import ru.matthew.exception.CurrencyNotFoundException;
import ru.matthew.exception.UnsupportedCurrencyException;

import java.util.Currency;
import java.util.Map;

@Service
@Slf4j
public class CurrencyService {
    private final CurrencyRateCacheService currencyRateCacheService;

    public CurrencyService(CurrencyRateCacheService currencyRateCacheService) {
        this.currencyRateCacheService = currencyRateCacheService;
    }

    public CurrencyRateResponse searchCurrencyRate(String currencyCode) {
        if (isValidCurrencyCode(currencyCode)) {
            log.warn("Не поддерживаемый код валюты: {}", currencyCode);
            throw new UnsupportedCurrencyException("Unsupported currency code: " + currencyCode);
        }

        Map<String, Double> rates = currencyRateCacheService.getCurrencyRates();
        log.debug("Полученные курсы валют: {}", rates);

        if (!rates.containsKey(currencyCode)) {
            log.error("Курс валюты не найден для кода: {}", currencyCode);
            throw new CurrencyNotFoundException("Currency rate not found for code: " + currencyCode);
        }

        CurrencyRateResponse response = new CurrencyRateResponse(currencyCode, rates.get(currencyCode));
        log.info("Курс валюты {} успешно получен: {}", currencyCode, response.getRate());
        return response;
    }

    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        validateCurrencyCodes(fromCurrency, toCurrency);

        Map<String, Double> rates = currencyRateCacheService.getCurrencyRates();
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
