package ru.matthew.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.matthew.dao.model.Location;
import ru.matthew.dao.model.PlaceCategory;
import ru.matthew.dao.repository.InMemoryStore;

@Configuration
public class DataSourceConfig {
    @Bean
    public InMemoryStore<String, Location> locationStore() {
        return new InMemoryStore<>();
    }

    @Bean
    public InMemoryStore<Integer, PlaceCategory> placeCategoryStore() {
        return new InMemoryStore<>();
    }
}
