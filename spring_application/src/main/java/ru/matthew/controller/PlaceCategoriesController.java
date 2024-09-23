package ru.matthew.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matthew.model.PlaceCategory;
import ru.matthew.service.KudaGoService;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/places/categories")
public class PlaceCategoriesController {

    private final KudaGoService kudaGoService;

    public PlaceCategoriesController(KudaGoService kudaGoService) {
        this.kudaGoService = kudaGoService;
    }

    @GetMapping
    public Collection<PlaceCategory> getAllPlaceCategories() {
        return kudaGoService.getPlaceCategoryStore().getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceCategory> getPlaceCategoryById(@PathVariable int id) {
        return kudaGoService.getPlaceCategoryStore().get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlaceCategory> createPlaceCategory(@RequestBody PlaceCategory placeCategory) {
        if (kudaGoService.getPlaceCategoryStore().get(placeCategory.getId()).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        kudaGoService.getPlaceCategoryStore().save(placeCategory);
        return ResponseEntity.ok(placeCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceCategory> updatePlaceCategory(@PathVariable int id, @RequestBody PlaceCategory placeCategory) {
        if (kudaGoService.getPlaceCategoryStore().get(id).isPresent()) {
            placeCategory.setId(id);
            kudaGoService.getPlaceCategoryStore().update(placeCategory);
            return ResponseEntity.ok(placeCategory);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaceCategory(@PathVariable int id) {
        kudaGoService.getPlaceCategoryStore().delete(id);
        return ResponseEntity.noContent().build();
    }
}
