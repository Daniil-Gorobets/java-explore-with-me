package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicEventController {

    @Autowired
    private final PublicEventService publicEventService;


    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.info("PublicEventController - GET: /events text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, " +
                "onlyAvailable={}, sort={}, from={}, size={}, request={}", text,  categories,  paid,  rangeStart,
                rangeEnd, onlyAvailable,  sort,  from, size, request.toString());
        return publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(
            @PathVariable @NotNull @Positive Long id,
            HttpServletRequest request) {
        log.info("PublicEventController - GET: /events/{} request={}", id, request.toString());
        return publicEventService.getEventById(id, request);
    }
}
