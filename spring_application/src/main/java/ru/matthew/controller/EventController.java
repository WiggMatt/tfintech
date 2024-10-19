package ru.matthew.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.matthew.dto.EventRequestDTO;
import ru.matthew.dto.EventResponseDTO;
import ru.matthew.service.EventService;

@RestController
@RequestMapping("/api/v1/events")
@Validated
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public Mono<EventResponseDTO> getEvents(@Valid @RequestBody EventRequestDTO request) {
        return eventService.fetchEvents(
                request.getBudget(),
                request.getCurrency(),
                request.getDateFrom(),
                request.getDateTo()
        );
    }
}
