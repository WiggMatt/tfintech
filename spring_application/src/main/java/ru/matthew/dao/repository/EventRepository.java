package ru.matthew.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import ru.matthew.dao.model.Event;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    boolean existsByTitleAndDateAndLocationSlug(@Param("title") String title,
                                                @Param("date") LocalDate date,
                                                @Param("locationSlug") String locationSlug);
}
