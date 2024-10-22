package ru.matthew.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.matthew.dao.model.Location;
import ru.matthew.dto.LocationDTO;
import ru.matthew.dto.SuccessJsonDTO;
import ru.matthew.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Получение всех локаций
    @GetMapping
    public List<LocationDTO> getAllLocations() {
        log.debug("Запрос всех локаций");
        List<LocationDTO> locations = locationService.getAllLocations();
        log.info("Успешно получены все локации");
        return locations;
    }

    // Получение конкретной локации по slug с жадной загрузкой событий
    @GetMapping("/{slug}")
    public Location getLocationBySlug(@PathVariable String slug) {
        log.debug("Запрос локации по slug: {}", slug);
        Location location = locationService.getLocationBySlugWithEvents(slug);
        log.info("Локация с slug {} найдена", slug);
        return location;
    }

    // Создание локации
    @PostMapping
    public SuccessJsonDTO createLocation(@Valid @RequestBody Location location) {
        locationService.createLocation(location);
        log.info("Локация с slug {} успешно создана", location.getSlug());
        return new SuccessJsonDTO("Локация успешно создана");
    }

    // Обновление локации
    @PutMapping("/{slug}")
    public SuccessJsonDTO updateLocation(@PathVariable String slug, @Valid @RequestBody Location location) {
        locationService.updateLocation(slug, location);
        log.info("Локация с slug {} успешно обновлена", slug);
        return new SuccessJsonDTO("Локация успешно обновлена");
    }

    // Удаление локации
    @DeleteMapping("/{slug}")
    public SuccessJsonDTO deleteLocation(@PathVariable String slug) {
        locationService.deleteLocation(slug);
        log.info("Локация с slug {} успешно удалена", slug);
        return new SuccessJsonDTO("Локация успешно удалена");
    }
}
