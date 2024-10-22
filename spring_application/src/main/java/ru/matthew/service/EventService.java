package ru.matthew.service;


import org.springframework.stereotype.Service;
import ru.matthew.dao.model.Event;
import ru.matthew.dao.repository.EventRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents(String title, String locationSlug, LocalDate fromDate, LocalDate toDate) {
        return eventRepository.findEvents(EventRepository.buildSpecification(title, locationSlug, fromDate, toDate));
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        return optionalEvent.orElse(null);
    }

    public Event updateEvent(Long id, Event event) {
        if (eventRepository.existsById(id)) {
            event.setId(id); // Устанавливаем ID события, чтобы обновить его
            return eventRepository.save(event);
        }
        return null;
    }

    public void deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        }
    }
}
