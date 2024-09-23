package ru.matthew.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matthew.model.Location;
import ru.matthew.service.KudaGoService;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationsController {

    private final KudaGoService kudaGoService;

    public LocationsController(KudaGoService kudaGoService) {
        this.kudaGoService = kudaGoService;
    }

    @GetMapping
    public Collection<Location> getAllLocations() {
        return kudaGoService.getLocationStore().getAll();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Location> getLocationBySlug(@PathVariable String slug) {
        return kudaGoService.getLocationStore().get(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        kudaGoService.getLocationStore().save(location);
        return ResponseEntity.ok(location);
    }

    @PutMapping("/{slug}")
    public ResponseEntity<Location> updateLocation(@PathVariable String slug, @RequestBody Location location) {
        if (kudaGoService.getLocationStore().get(slug).isPresent()) {
            if (location.getName() == null || location.getSlug() == null) {
                ResponseEntity.badRequest().build();
            } else {
                location.setSlug(slug);
                kudaGoService.getLocationStore().update(location);
                return ResponseEntity.ok(location);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String slug) {
        kudaGoService.getLocationStore().delete(slug);
        return ResponseEntity.noContent().build();
    }
}
