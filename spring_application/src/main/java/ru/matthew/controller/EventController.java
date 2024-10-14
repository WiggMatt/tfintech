package ru.matthew.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.matthew.dto.EventRequestDTO;
import ru.matthew.dto.EventResponseDTO;
import ru.matthew.service.EventService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public CompletableFuture<EventResponseDTO> getEvents(@Valid @RequestBody EventRequestDTO request) {
        return eventService.fetchEvents(
                request.getBudget(),
                request.getCurrency(),
                request.getDateFrom(),
                request.getDateTo()
        );
    }
}
