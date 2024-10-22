package ru.matthew.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.matthew.dao.model.Location;

public interface LocationRepository extends JpaRepository<Location, String> {
    @Query(value = "FROM Location l LEFT JOIN FETCH l.events WHERE l.slug = :slug")
    Location findBySlugWithEvents(@Param("slug") String slug);
}
