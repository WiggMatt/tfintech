package ru.matthew.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.matthew.aspect.Timed;
import ru.matthew.model.Location;
import ru.matthew.service.KudaGoService;

import java.util.Collection;
import java.util.Optional;

@Timed
@RestController
@RequestMapping("/api/v1/locations")
@Validated
@Slf4j
public class LocationsController {

    private final KudaGoService kudaGoService;

    @Autowired
    public LocationsController(KudaGoService kudaGoService) {
        this.kudaGoService = kudaGoService;
    }

    @GetMapping
    public ResponseEntity<Collection<Location>> getAllLocations() {
        log.debug("Запрос всех локаций");
        Collection<Location> locations = kudaGoService.getLocationStore().getAll();
        log.info("Успешно получены все локации");
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Location> getLocationBySlug(@PathVariable String slug) {
        log.debug("Запрос локации по slug: {}", slug);
        Optional<Location> location = kudaGoService.getLocationStore().get(slug);
        if (location.isPresent()) {
            log.info("Локация с slug {} найдена", slug);
            return ResponseEntity.ok(location.get());
        } else {
            log.warn("Локация с slug {} не найдена", slug);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createLocation(@RequestBody Location location) {
        if (location.getName() == null || location.getSlug() == null) {
            log.error("Ошибка создания локации: обязательные поля отсутствуют (Name: {}, Slug: {})",
                    location.getName(), location.getSlug());
            return ResponseEntity.badRequest().body("Name и Slug обязательны.");
        }

        Optional<Location> existingLocation = kudaGoService.getLocationStore().get(location.getSlug());
        if (existingLocation.isPresent()) {
            log.warn("Ошибка создания локации: локация с slug {} уже существует", location.getSlug());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Локация с таким Slug уже существует.");
        }

        kudaGoService.getLocationStore().save(location);
        log.info("Локация с slug {} успешно создана", location.getSlug());
        return ResponseEntity.status(HttpStatus.CREATED).body("Локация успешно создана.");
    }

    @PutMapping("/{slug}")
    public ResponseEntity<String> updateLocation(@PathVariable String slug, @RequestBody Location location) {
        if (location.getName() == null) {
            log.error("Ошибка обновления локации: название отсутствует (Slug: {})", slug);
            return ResponseEntity.badRequest().body("Название обязательно.");
        }

        Optional<Location> existingLocation = kudaGoService.getLocationStore().get(slug);
        if (existingLocation.isEmpty()) {
            log.warn("Ошибка обновления локации: локация с slug {} не найдена", slug);
            return ResponseEntity.notFound().build();
        }

        location.setSlug(slug);
        kudaGoService.getLocationStore().update(location);
        log.info("Локация с slug {} успешно обновлена", slug);
        return ResponseEntity.ok("Локация успешно обновлена.");
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<String> deleteLocation(@PathVariable String slug) {
        Optional<Location> existingLocation = kudaGoService.getLocationStore().get(slug);
        if (existingLocation.isEmpty()) {
            log.warn("Ошибка удаления локации: локация с slug {} не найдена", slug);
            return ResponseEntity.notFound().build();
        }

        kudaGoService.getLocationStore().delete(slug);
        log.info("Локация с slug {} успешно удалена", slug);
        return ResponseEntity.ok("Локация успешно удалена.");
    }

}
