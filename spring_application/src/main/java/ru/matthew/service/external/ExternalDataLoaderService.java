package ru.matthew.service.external;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.matthew.model.Location;
import ru.matthew.model.PlaceCategory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@NoArgsConstructor
public class ExternalDataLoaderService {

    public List<Location> fetchLocationsFromApi() {
        String url = "https://kudago.com/public-api/v1.4/locations/";
        return fetchFromApi(url, Location[].class);
    }

    public List<PlaceCategory> fetchCategoriesFromApi() {
        String url = "https://kudago.com/public-api/v1.4/place-categories/";
        return fetchFromApi(url, PlaceCategory[].class);
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
