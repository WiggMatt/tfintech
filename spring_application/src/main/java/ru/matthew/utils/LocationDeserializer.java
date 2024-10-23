package ru.matthew.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;
import ru.matthew.dao.model.Location;
import ru.matthew.dao.repository.LocationRepository;
import ru.matthew.exception.ElementWasNotFoundException;

import java.io.IOException;

@Component
public class LocationDeserializer extends JsonDeserializer<Location> {

    private final LocationRepository locationRepository;

    public LocationDeserializer(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String slug = p.getText();
        return locationRepository.findById(slug)
                .orElseThrow(() -> new ElementWasNotFoundException("Локация с таким slug не найдена"));
    }
}
