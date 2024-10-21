package ru.matthew.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matthew.dao.model.Location;

public interface LocationRepository extends JpaRepository<Location, String> {
}
