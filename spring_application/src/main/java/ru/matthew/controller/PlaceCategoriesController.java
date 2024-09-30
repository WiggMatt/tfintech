package ru.matthew.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.matthew.aop.Timed;
import ru.matthew.dto.SuccessJsonDTO;
import ru.matthew.model.PlaceCategory;
import ru.matthew.service.PlaceCategoryService;

import java.util.Collection;

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
    public Collection<PlaceCategory> getAllPlaceCategories() {
        log.debug("Получение всех категорий мест");
        Collection<PlaceCategory> categories = placeCategoryService.getAllPlaceCategories();
        log.info("Успешно получены все категории мест");
        return categories;
    }

    @GetMapping("/{id}")
    public PlaceCategory getPlaceCategoryById(@PathVariable int id) {
        log.debug("Запрос категории места по id: {}", id);
        PlaceCategory category = placeCategoryService.getPlaceCategoryById(id);
        log.info("Категория места с id {} найдена", id);
        return category;
    }

    @PostMapping
    public SuccessJsonDTO createPlaceCategory(@RequestBody PlaceCategory placeCategory) {
        placeCategoryService.createPlaceCategory(placeCategory);
        log.info("Категория места с id {} успешно создана", placeCategory.getId());
        return new SuccessJsonDTO("Категория места успешно создана");
    }

    @PutMapping("/{id}")
    public SuccessJsonDTO updatePlaceCategory(@PathVariable int id, @RequestBody PlaceCategory placeCategory) {
        placeCategoryService.updatePlaceCategory(id, placeCategory);
        log.info("Категория места с id {} успешно обновлена", id);
        return new SuccessJsonDTO("Категория места успешно обновлена");
    }

    @DeleteMapping("/{id}")
    public SuccessJsonDTO deletePlaceCategory(@PathVariable int id) {
        placeCategoryService.deletePlaceCategory(id);
        log.info("Категория места с id {} успешно удалена", id);
        return new SuccessJsonDTO("Категория места успешно удалена");
    }
}
