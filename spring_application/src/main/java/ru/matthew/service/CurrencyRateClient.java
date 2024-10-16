package ru.matthew.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.matthew.dto.CurrencyConversionRequest;
import ru.matthew.dto.CurrencyConversionResponse;

@Service
public class CurrencyRateClient {
    private final WebClient webClient;

    public CurrencyRateClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        Mono<CurrencyConversionResponse> responseMono = webClient.post()
                .uri("/currencies/convert")
                .bodyValue(new CurrencyConversionRequest(fromCurrency, toCurrency, amount))
                .retrieve()
                .bodyToMono(CurrencyConversionResponse.class);

        CurrencyConversionResponse response = responseMono.block();
        return response.getConvertedAmount();
    }
}