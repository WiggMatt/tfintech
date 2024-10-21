package ru.matthew.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.matthew.aop.Timed;
import ru.matthew.dto.SuccessJsonDTO;
import ru.matthew.dao.model.Location;
import ru.matthew.service.LocationService;

import java.util.Collection;

@Timed
@RestController
@RequestMapping("/api/v1/locations")
@Slf4j
public class LocationsController {

    private final LocationService locationService;

    @Autowired
    public LocationsController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public Collection<Location> getAllLocations() {
        log.debug("Запрос всех локаций");
        Collection<Location> locations = locationService.getAllLocations();
        log.info("Успешно получены все локации");
        return locations;
    }

    @GetMapping("/{slug}")
    public Location getLocationBySlug(@PathVariable String slug) {
        log.debug("Запрос локации по slug: {}", slug);
        Location location = locationService.getLocationBySlug(slug);
        log.info("Локация с slug {} найдена", slug);
        return location;
    }

    @PostMapping
    public SuccessJsonDTO createLocation(@RequestBody Location location) {
        locationService.createLocation(location);
        log.info("Локация с slug {} успешно создана", location.getSlug());
        return new SuccessJsonDTO("Локация успешно создана");
    }

    @PutMapping("/{slug}")
    public SuccessJsonDTO updateLocation(@PathVariable String slug, @RequestBody Location location) {
        locationService.updateLocation(slug, location);
        log.info("Локация с slug {} успешно обновлена", slug);
        return new SuccessJsonDTO("Локация успешно обновлена");
    }

    @DeleteMapping("/{slug}")
    public SuccessJsonDTO deleteLocation(@PathVariable String slug) {
        locationService.deleteLocation(slug);
        log.info("Локация с slug {} успешно удалена", slug);
        return new SuccessJsonDTO("Локация успешно удалена");
    }
}
