package ru.matthew.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.matthew.dao.model.Event;
import ru.matthew.dao.repository.EventRepository;
import ru.matthew.exception.ElementAlreadyExistsException;
import ru.matthew.exception.ElementWasNotFoundException;
import ru.matthew.utils.EventSpecification;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class EventService {

    private final EventSpecification eventSpecification;
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventSpecification eventSpecification, EventRepository eventRepository) {
        this.eventSpecification = eventSpecification;
        this.eventRepository = eventRepository;
    }

    public List<Event> getEventsByFilter(String title, String locationSlug, LocalDate fromDate, LocalDate toDate) {
        if (eventRepository.findAll().isEmpty()) {
            String message = "Список с событиями пуст";
            log.warn(message);
            throw new ElementWasNotFoundException(message);
        }

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            String message = "Дата fromDate не может быть больше даты toDate";
            log.warn(message);
            throw new IllegalArgumentException(message);
        }

        Specification<Event> specification = eventSpecification.buildSpecification(title, locationSlug, fromDate, toDate);

        List<Event> events = eventRepository.findAll(specification);

        if (events.isEmpty()) {
            String message = "Нет подходящих событий под заданные параметры";
            log.warn(message);
            throw new ElementWasNotFoundException(message);
        }

        return events;
    }

    public void createEvent(Event event) {
        boolean eventExists = eventRepository.existsByTitleAndDateAndLocationSlug(
                event.getTitle(),
                event.getDate(),
                event.getLocation().getSlug());
        if (eventExists) {
            log.warn("Ошибка создания события: событие с названием '{}' уже существует в локации '{}' на дату '{}'",
                    event.getTitle(), event.getLocation().getSlug(), event.getDate());
            throw new ElementAlreadyExistsException("Событие с таким названием уже существует в этой локации на эту дату");
        }

        eventRepository.save(event);
    }

    public void updateEvent(Long id, Event event) {
        checkEventNotExists(id);

        event.setId(id);
        eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        checkEventNotExists(id);
        eventRepository.deleteById(id);
    }

    private void checkEventNotExists(Long id) {
        if (!eventRepository.existsById(id)) {
            log.warn("Событие с ID {} не найдено", id);
            throw new ElementWasNotFoundException("Событие с таким ID не найдено");
        }
    }
}
