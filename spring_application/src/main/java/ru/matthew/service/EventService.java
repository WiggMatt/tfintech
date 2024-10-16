package ru.matthew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.matthew.dto.EventDTO;
import ru.matthew.dto.EventResponseDTO;
import ru.matthew.utils.RateLimiter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
@Slf4j
public class EventService {
    @Value("${kudago.api.url}")
    private String kudaGoApiUrl;

    private final WebClient webClient;
    private final RateLimiter rateLimiter;
    private final CurrencyRateClient currencyRateClient;


    @Autowired
    public EventService(WebClient.Builder webClientBuilder, RateLimiter rateLimiter, CurrencyRateClient currencyRateClient) {
        this.webClient = webClientBuilder.build();
        this.rateLimiter = rateLimiter;
        this.currencyRateClient = currencyRateClient;
    }

    public Mono<EventResponseDTO> fetchEvents(double budget, String currency, String dateFrom, String dateTo) {
        LocalDate[] dates = determineDates(dateFrom, dateTo);
        LocalDate fromDate = dates[0];
        LocalDate toDate = dates[1];

        String requestUrl = String.format("%s?fields=id,title,description,place,dates,price&location=kzn&actual_since=%s&actual_until=%s",
                kudaGoApiUrl,
                fromDate.atStartOfDay(ZoneId.of("UTC")).toEpochSecond(),
                toDate.atStartOfDay(ZoneId.of("UTC")).toEpochSecond());

        log.debug("Сформированный URL для запроса событий: {}", requestUrl);

        Mono<List<EventDTO>> eventsMono = fetchEventsReactive(requestUrl);
        Mono<Double> budgetMono = Mono.fromCallable(() -> convertBudget(budget, currency));

        return Mono.zip(eventsMono, budgetMono, (events, budgetInRUB) -> {
                    log.info("Получено {} событий, фильтрация по бюджету: {} RUB", events.size(), budgetInRUB);
                    List<EventDTO> suitableEvents = filterEventsByBudget(events, budgetInRUB);
                    return new EventResponseDTO(suitableEvents.size(), suitableEvents);
                })
                .onErrorReturn(new EventResponseDTO(0, List.of()));
    }

    private LocalDate[] determineDates(String dateFrom, String dateTo) {
        LocalDate today = LocalDate.now();
        LocalDate fromDate;
        LocalDate toDate;

        if (dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty()) {
            fromDate = LocalDate.parse(dateFrom);
            toDate = LocalDate.parse(dateTo);
        } else {
            fromDate = today.with(ChronoField.DAY_OF_WEEK, 1);
            toDate = today.with(ChronoField.DAY_OF_WEEK, 7);
        }
        log.debug("Определенные даты: от {} до {}", fromDate, toDate);
        return new LocalDate[]{fromDate, toDate};
    }

    private Mono<List<EventDTO>> fetchEventsReactive(String requestUrl) {
        return Mono.fromCallable(() -> rateLimiter.executeWithLimit(() -> webClient.get()
                .uri(requestUrl)
                .retrieve()
                .bodyToMono(EventResponseDTO.class)
                .flatMap(response -> {
                    if (response != null && response.getResults() != null) {
                        return Mono.just(response.getResults());
                    }
                    return Mono.just(List.<EventDTO>of());
                })
                .doOnError(e -> log.error("Ошибка при запросе событий: {}", e.getMessage(), e))
                .block())).flatMap(Mono::just);
    }

    private List<EventDTO> filterEventsByBudget(List<EventDTO> events, double budgetInRUB) {
        List<EventDTO> suitableEvents = events.stream()
                .filter(event -> parsePrice(event.getPrice()) <= budgetInRUB)
                .toList();
        log.info("Найдено подходящих событий: {}", suitableEvents.size());
        return suitableEvents;
    }

    private double parsePrice(String priceString) {
        try {
            String numericString = priceString.replaceAll("[^\\d.]", "");
            double price = Double.parseDouble(numericString);
            log.debug("Парсинг цены из строки '{}': {}", priceString, price);
            return price;
        } catch (NumberFormatException e) {
            log.error("Ошибка при парсинге цены: {}", priceString, e);
            return Double.MAX_VALUE;
        }
    }

    private double convertBudget(double budget, String currency) {
        log.debug("Конвертация бюджета {} {} в RUB", budget, currency);
        return currencyRateClient.convertCurrency(currency, "RUB", budget);
    }
}

