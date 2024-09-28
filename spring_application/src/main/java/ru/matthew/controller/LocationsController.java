package ru.matthew.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matthew.aspect.Timed;
import ru.matthew.exception.ElementAlreadyExistsException;
import ru.matthew.exception.ElementWasNotFoundException;
import ru.matthew.model.Location;
import ru.matthew.service.LocationService;

import java.util.Collection;
import java.util.Optional;

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
    public ResponseEntity<?> getAllLocations() {
        try {
            log.debug("Запрос всех локаций");
            Collection<Location> locations = locationService.getAllLocations();
            log.info("Успешно получены все локации");
            return ResponseEntity.ok(locations);
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getLocationBySlug(@PathVariable String slug) {
        try {
            log.debug("Запрос локации по slug: {}", slug);
            Optional<Location> location = locationService.getLocationBySlug(slug);
            log.info("Локация с slug {} найдена", slug);
            return ResponseEntity.ok(location);
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createLocation(@RequestBody Location location) {
        try {
            locationService.createLocation(location);
            log.info("Локация с slug {} успешно создана", location.getSlug());
            return ResponseEntity.status(HttpStatus.CREATED).body("Локация успешно создана.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ElementAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{slug}")
    public ResponseEntity<String> updateLocation(@PathVariable String slug, @RequestBody Location location) {
        try {
            locationService.updateLocation(slug, location);
            log.info("Локация с slug {} успешно обновлена", slug);
            return ResponseEntity.ok("Локация успешно обновлена.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<String> deleteLocation(@PathVariable String slug) {
        try {
            locationService.deleteLocation(slug);
            log.info("Локация с slug {} успешно удалена", slug);
            return ResponseEntity.ok("Локация успешно удалена.");
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
