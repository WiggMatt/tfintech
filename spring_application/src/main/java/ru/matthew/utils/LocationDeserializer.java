package ru.matthew.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.matthew.dao.model.Location;
import ru.matthew.dao.repository.LocationRepository;
import ru.matthew.exception.ElementWasNotFoundException;

import java.io.IOException;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class LocationDeserializer extends JsonDeserializer<Location> {

    private LocationRepository locationRepository;

    @Override
    public Location deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String slug = p.getText();
        return locationRepository.findById(slug)
                .orElseThrow(() -> new ElementWasNotFoundException("Локация с таким slug не найдена"));
    }
}
