package ru.matthew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.matthew.aspect.Timed;
import ru.matthew.model.Location;
import ru.matthew.model.PlaceCategory;

import java.util.List;

@Service
@Slf4j
public class DataInitializerService {

    private final ExternalDataLoaderService externalDataLoaderService;
    private final LocationService locationService;
    private final PlaceCategoryService placeCategoryService;

    @Autowired
    public DataInitializerService(ExternalDataLoaderService externalDataLoaderService,
                                  LocationService locationService,
                                  PlaceCategoryService placeCategoryService) {
        this.externalDataLoaderService = externalDataLoaderService;
        this.locationService = locationService;
        this.placeCategoryService = placeCategoryService;
    }

    @Timed
    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        log.info("Начало инициализации данных");

        try {
            initCategories();
            initLocations();
        } catch (Exception e) {
            log.error("Ошибка при инициализации данных", e);
        }

        log.info("Инициализация данных завершена");
    }

    private void initCategories() {
        List<PlaceCategory> placeCategories = externalDataLoaderService.fetchCategoriesFromApi();
        placeCategories.forEach(placeCategoryService::createPlaceCategory);
        log.info("Загружено категорий: {}", placeCategories.size());
    }

    private void initLocations() {
        List<Location> locations = externalDataLoaderService.fetchLocationsFromApi();
        locations.forEach(locationService::createLocation);
        log.info("Загружено городов: {}", locations.size());
    }
}
