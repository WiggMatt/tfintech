package ru.matthew.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.matthew.aspect.Timed;
import ru.matthew.exception.ElementAlreadyExistsException;
import ru.matthew.exception.ElementWasNotFoundException;
import ru.matthew.model.Location;
import ru.matthew.model.PlaceCategory;
import ru.matthew.service.PlaceCategoryService;

import java.util.Collection;
import java.util.Optional;

@Timed
@RestController
@RequestMapping("/api/v1/places/categories")
@Validated
@Slf4j
public class PlaceCategoriesController {

    private final PlaceCategoryService placeCategoryService;

    @Autowired
    public PlaceCategoriesController(PlaceCategoryService placeCategoryService) {
        this.placeCategoryService = placeCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPlaceCategories() {
        try {
            log.debug("Получение всех категорий мест");
            Collection<PlaceCategory> locations = placeCategoryService.getAllPlaceCategories();
            log.info("Успешно получены все категории мест");
            return ResponseEntity.ok(locations);
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaceCategoryById(@PathVariable int id) {
        try {
            log.debug("Запрос категории места по id: {}", id);
            Optional<PlaceCategory> location = placeCategoryService.getPlaceCategoryById(id);
            log.info("Категория места с id {} найдена", id);
            return ResponseEntity.ok(location);
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createPlaceCategory(@RequestBody PlaceCategory placeCategory) {
        try {
            placeCategoryService.createPlaceCategory(placeCategory);
            log.info("Категория места с id {} успешно создана", placeCategory.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Категория места успешно создана");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ElementAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePlaceCategory(@PathVariable int id, @RequestBody PlaceCategory placeCategory) {
        try {
            placeCategoryService.updatePlaceCategory(id, placeCategory);
            log.info("Категория места с id {} успешно обновлена", id);
            return ResponseEntity.ok("Категория места успешно обновлена");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaceCategory(@PathVariable int id) {
        try {
            placeCategoryService.deletePlaceCategory(id);
            log.info("Категория места с id {} успешно удалена", id);
            return ResponseEntity.ok("Категория места успешно удалена");
        } catch (ElementWasNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
