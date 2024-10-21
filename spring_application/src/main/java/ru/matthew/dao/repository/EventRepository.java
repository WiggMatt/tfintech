package ru.matthew.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matthew.dao.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
