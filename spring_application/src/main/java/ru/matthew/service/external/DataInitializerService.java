package ru.matthew.service.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.matthew.aop.Timed;
import ru.matthew.exception.ElementAlreadyExistsException;
import ru.matthew.model.Location;
import ru.matthew.model.PlaceCategory;
import ru.matthew.service.LocationService;
import ru.matthew.service.PlaceCategoryService;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
@Timed
public class DataInitializerService {

    private final ExternalDataLoaderService externalDataLoaderService;
    private final LocationService locationService;
    private final PlaceCategoryService placeCategoryService;
    @Qualifier("fixedThreadPool")
    private final ExecutorService fixedThreadPool;
    @Qualifier("scheduledThreadPool")
    private final ScheduledExecutorService scheduledThreadPool;
    @Value("${app.init.schedule.duration}")
    private Duration scheduleDuration;

    public DataInitializerService(
            ExternalDataLoaderService externalDataLoaderService,
            LocationService locationService,
            PlaceCategoryService placeCategoryService,
            ExecutorService fixedThreadPool,
            ScheduledExecutorService scheduledThreadPool) {
        this.externalDataLoaderService = externalDataLoaderService;
        this.locationService = locationService;
        this.placeCategoryService = placeCategoryService;
        this.fixedThreadPool = fixedThreadPool;
        this.scheduledThreadPool = scheduledThreadPool;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        long delayInSeconds = scheduleDuration.getSeconds();
        scheduledThreadPool.scheduleWithFixedDelay(this::initializeData, 0, delayInSeconds, TimeUnit.SECONDS);
    }

    private void initializeData() {
        log.info("Начало инициализации данных");
        Future<?> categoriesFuture = fixedThreadPool.submit(this::initCategories);
        Future<?> locationsFuture = fixedThreadPool.submit(this::initLocations);

        try {
            categoriesFuture.get();
            locationsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при инициализации данных", e);
        }
        log.info("Инициализация данных завершена");
    }

    private void initCategories() {
        List<PlaceCategory> placeCategories = externalDataLoaderService.fetchCategoriesFromApi();
        int addedCount = 0;

        for (PlaceCategory placeCategory : placeCategories) {
            try {
                placeCategoryService.createPlaceCategory(placeCategory);
                addedCount++;
            } catch (ElementAlreadyExistsException ignored) {
            }
        }
        log.info("Загружено категорий: {}. Всего категорий: {}", addedCount, placeCategoryService.getAllPlaceCategories().size());
    }

    private void initLocations() {
        List<Location> locations = externalDataLoaderService.fetchLocationsFromApi();
        int addedCount = 0;

        for (Location location : locations) {
            try {
                locationService.createLocation(location);
                addedCount++;
            } catch (ElementAlreadyExistsException ignored) {
            }
        }
        log.info("Загружено городов: {}. Всего городов: {}", addedCount, locationService.getAllLocations().size());
    }
}
