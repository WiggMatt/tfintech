package ru.matthew.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.matthew.aspect.Timed;
import ru.matthew.model.PlaceCategory;
import ru.matthew.service.KudaGoService;

import java.util.Collection;
import java.util.Optional;

@Timed
@RestController
@RequestMapping("/api/v1/places/categories")
@Validated
@Slf4j
public class PlaceCategoriesController {

    private final KudaGoService kudaGoService;

    @Autowired
    public PlaceCategoriesController(KudaGoService kudaGoService) {
        this.kudaGoService = kudaGoService;
    }

    @GetMapping
    public ResponseEntity<Collection<PlaceCategory>> getAllPlaceCategories() {
        log.debug("Получение всех категорий мест");
        Collection<PlaceCategory> categories = kudaGoService.getPlaceCategoryStore().getAll();
        log.info("Успешно получены все категории мест");
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceCategory> getPlaceCategoryById(@PathVariable int id) {
        log.debug("Запрос категории места по ID: {}", id);
        Optional<PlaceCategory> placeCategory = kudaGoService.getPlaceCategoryStore().get(id);
        if (placeCategory.isPresent()) {
            log.info("Категория места с ID {} найдена", id);
            return ResponseEntity.ok(placeCategory.get());
        } else {
            log.warn("Категория места с ID {} не найдена", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createPlaceCategory(@RequestBody PlaceCategory placeCategory) {
        if (placeCategory.getId() == 0 || placeCategory.getName() == null) {
            log.error("Ошибка создания категории места: обязательные поля отсутствуют (ID: {}, Name: {})",
                    placeCategory.getId(), placeCategory.getName());
            return ResponseEntity.badRequest().body("ID и название обязательны.");
        }

        Optional<PlaceCategory> existingCategory = kudaGoService.getPlaceCategoryStore().get(placeCategory.getId());
        if (existingCategory.isPresent()) {
            log.warn("Ошибка создания категории места: категория с ID {} уже существует", placeCategory.getId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Категория с таким ID уже существует.");
        }

        kudaGoService.getPlaceCategoryStore().save(placeCategory);
        log.info("Категория места с ID {} успешно создана", placeCategory.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Категория места успешно создана.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePlaceCategory(@PathVariable int id, @RequestBody PlaceCategory placeCategory) {
        if (placeCategory.getName() == null) {
            log.error("Ошибка обновления категории места: название отсутствует (ID: {})", id);
            return ResponseEntity.badRequest().body("Название обязательно.");
        }

        Optional<PlaceCategory> existingCategory = kudaGoService.getPlaceCategoryStore().get(id);
        if (existingCategory.isEmpty()) {
            log.warn("Ошибка обновления категории места: категория с ID {} не найдена", id);
            return ResponseEntity.notFound().build();
        }

        placeCategory.setId(id);
        kudaGoService.getPlaceCategoryStore().update(placeCategory);
        log.info("Категория места с ID {} успешно обновлена", id);
        return ResponseEntity.ok("Категория места успешно обновлена.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaceCategory(@PathVariable int id) {
        Optional<PlaceCategory> existingCategory = kudaGoService.getPlaceCategoryStore().get(id);
        if (existingCategory.isEmpty()) {
            log.warn("Ошибка удаления категории места: категория с ID {} не найдена", id);
            return ResponseEntity.notFound().build();
        }

        kudaGoService.getPlaceCategoryStore().delete(id);
        log.info("Категория места с ID {} успешно удалена", id);
        return ResponseEntity.ok("Категория места успешно удалена.");
    }
}
