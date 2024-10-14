package ru.matthew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.matthew.dto.EventDTO;
import ru.matthew.dto.EventResponseDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EventService {
    @Value("${kudago.api.url}")
    private String kudaGoApiUrl;

    private final RestTemplate restTemplate;
    private final CurrencyConverterService currencyConverterService;

    @Autowired
    public EventService(RestTemplate restTemplate, CurrencyConverterService currencyConverterService) {
        this.restTemplate = restTemplate;
        this.currencyConverterService = currencyConverterService;
    }

    public CompletableFuture<EventResponseDTO> fetchEvents(double budget, String currency, String dateFrom, String dateTo) {
        log.info("Запрос событий с бюджетом: {} {}, с датами от {} до {}", budget, currency, dateFrom, dateTo);

        LocalDate[] dates = determineDates(dateFrom, dateTo);
        LocalDate fromDate = dates[0];
        LocalDate toDate = dates[1];

        String requestUrl = String.format("%s?fields=id,title,description,place,dates,price&location=kzn&actual_since=%s&actual_until=%s",
                kudaGoApiUrl,
                fromDate.atStartOfDay(ZoneId.of("UTC")).toEpochSecond(),
                toDate.atStartOfDay(ZoneId.of("UTC")).toEpochSecond());

        log.debug("Сформированный URL для запроса событий: {}", requestUrl);

        CompletableFuture<List<EventDTO>> eventsFuture = fetchEventsAsync(requestUrl);
        CompletableFuture<Double> budgetFuture = CompletableFuture.supplyAsync(() -> convertBudget(budget, currency));

        return eventsFuture
                .thenCombine(budgetFuture, (events, budgetInRUB) -> {
                    log.info("Получено {} событий, фильтрация по бюджету: {} RUB", events.size(), budgetInRUB);
                    List<EventDTO> suitableEvents = filterEventsByBudget(events, budgetInRUB);
                    return new EventResponseDTO(suitableEvents.size(), suitableEvents);
                })
                .exceptionally(e -> {
                    log.error("Ошибка при получении событий: {}", e.getMessage(), e);
                    return new EventResponseDTO(0, List.of());
                });
    }

    private LocalDate[] determineDates(String dateFrom, String dateTo) {
        LocalDate today = LocalDate.now();
        LocalDate fromDate;
        LocalDate toDate;

        if (dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty()) {
            fromDate = LocalDate.parse(dateFrom);
            toDate = LocalDate.parse(dateTo);
        } else {
            fromDate = today.with(ChronoField.DAY_OF_WEEK, 1); // Понедельник
            toDate = today.with(ChronoField.DAY_OF_WEEK, 7); // Воскресенье
        }
        log.debug("Определенные даты: от {} до {}", fromDate, toDate);
        return new LocalDate[]{fromDate, toDate};
    }

    private CompletableFuture<List<EventDTO>> fetchEventsAsync(String requestUrl) {
        return CompletableFuture.supplyAsync(() -> {
            log.debug("Запрос событий по URL: {}", requestUrl);
            EventResponseDTO response = restTemplate.getForObject(requestUrl, EventResponseDTO.class);
            return response != null ? response.getResults() : List.of();
        });
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
        return currencyConverterService.convertCurrency(currency, "RUB", budget);
    }
}
