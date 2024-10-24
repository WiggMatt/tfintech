package ru.matthew.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.matthew.exception.ElementAlreadyExistsException;
import ru.matthew.exception.ElementWasNotFoundException;
import ru.matthew.model.Location;
import ru.matthew.repository.InMemoryStore;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {
    private final InMemoryStore<String, Location> locationStore;

    public Collection<Location> getAllLocations() {
        if (locationStore.getAll().isEmpty()) {
            log.warn("Список с локациями пуст");
            throw new ElementWasNotFoundException("Список с локациями пуст");
        }

        return locationStore.getAll();
    }

    public Location getLocationBySlug(String slug) {
        return locationStore.get(slug)
                .orElseThrow(() -> {
                    log.warn("Ошибка поиска локации: локация с slug {} не найдена", slug);
                    return new ElementWasNotFoundException("Локация с таким slug не найдена");
                });
    }

    public void createLocation(Location location) {
        if (location.getName() == null ||
                location.getName().isEmpty() ||
                location.getSlug() == null ||
                location.getSlug().isEmpty()) {
            log.error("Ошибка создания локации: обязательные поля отсутствуют (Name: {}, Slug: {})",
                    location.getName(), location.getSlug());
            throw new IllegalArgumentException("Название и slug обязательны");
        }
        if (locationStore.get(location.getSlug()).isPresent()) {
            log.warn("Ошибка создания локации: локация с slug {} уже существует", location.getSlug());
            throw new ElementAlreadyExistsException("Локация с таким slug уже существует");
        }

        locationStore.save(location);
    }

    public void updateLocation(String slug, Location location) {
        if (location.getName() == null) {
            log.error("Ошибка обновления локации: имя отсутствует");
            throw new IllegalArgumentException("Название обязательно");
        }
        if (locationStore.get(slug).isEmpty()) {
            log.warn("Ошибка обновления локации: локации с slug {} не существует", slug);
            throw new ElementWasNotFoundException("Локация с таким slug не найдена");
        }

        location.setSlug(slug);
        locationStore.update(location);
    }

    public void deleteLocation(String slug) {
        if (locationStore.get(slug).isEmpty()) {
            log.warn("Ошибка удаления локации: локация с slug {} не найдена", slug);
            throw new ElementWasNotFoundException("Локация с таким slug не найдена");
        }

        locationStore.delete(slug);
    }
}
