package ru.matthew.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.matthew.model.PlaceCategory;
import ru.matthew.model.Location;
import ru.matthew.repository.InMemoryStore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Slf4j
@Service
public class KudaGoService {
    private final InMemoryStore<Integer, PlaceCategory> placeCategoryStore = new InMemoryStore<>();
    private final InMemoryStore<String, Location> locationStore = new InMemoryStore<>();

    @PostConstruct
    public void init() {
        log.info("Начало инициализации данных из KudaGo API");

        try {
            initCategories();
            initLocations();
        } catch (Exception e) {
            log.error("Ошибка при инициалзиации данных", e);
        }

        log.info("Инициализация данных завершена");
    }

    private void initCategories() {
        String url = "https://kudago.com/public-api/v1.4/place-categories/";
        List<PlaceCategory> placeCategories = fetchFromApi(url, PlaceCategory[].class);
        placeCategories.forEach(placeCategoryStore::save);
        log.info("Загружено категорий: {}", placeCategories.size());
    }

    private void initLocations() {
        String url = "https://kudago.com/public-api/v1.4/locations/";
        List<Location> locations = fetchFromApi(url, Location[].class);
        locations.forEach(locationStore::save);
        log.info("Загружено городов: {}", locations.size());
    }

    private <T> List<T> fetchFromApi(String urlString, Class<T[]> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<T[]> response = restTemplate.getForEntity(urlString, clazz);
            T[] resultArray = response.getBody();
            return resultArray != null ? Arrays.asList(resultArray) : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            log.error("Ошибка при запросе к API: {}. {}", e.getStatusCode(), e.getMessage());
            return Collections.emptyList();
        }
    }
}
