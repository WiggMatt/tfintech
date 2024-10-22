package ru.matthew.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matthew.dao.model.Location;
import ru.matthew.dao.repository.LocationRepository;
import ru.matthew.dto.LocationDTO;
import ru.matthew.exception.ElementAlreadyExistsException;
import ru.matthew.exception.ElementWasNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationBySlugWithEvents(String slug) {
        checkLocationExists(slug, false);

        return locationRepository.findBySlugWithEvents(slug);
    }

    public List<LocationDTO> getAllLocations() {
        List<Location> locations = locationRepository.findAll();

        if (locations.isEmpty()) {
            log.warn("Список с локациями пуст");
            throw new ElementWasNotFoundException("Список с локациями пуст");
        }

        return locations.stream()
                .map(location -> new LocationDTO(location.getSlug(), location.getName()))
                .collect(Collectors.toList());
    }

    public void createLocation(Location location) {
        checkLocationExists(location.getSlug(), true);

        locationRepository.save(location);
    }

    public void updateLocation(String slug, Location location) {
        checkLocationExists(slug, false);

        location.setSlug(slug);
        locationRepository.save(location);
    }

    public void deleteLocation(String slug) {
        checkLocationExists(slug, false);

        locationRepository.deleteById(slug);
    }

    private void checkLocationExists(String slug, boolean forCreation) {
        boolean exists = locationRepository.findById(slug).isPresent();

        if (forCreation && exists) {
            log.warn("Ошибка создания локации: локация с slug {} уже существует", slug);
            throw new ElementAlreadyExistsException("Локация с таким slug уже существует");
        } else if (!forCreation && !exists) {
            log.warn("Ошибка: локация с slug {} не найдена", slug);
            throw new ElementWasNotFoundException("Локация с таким slug не найдена");
        }
    }
}
