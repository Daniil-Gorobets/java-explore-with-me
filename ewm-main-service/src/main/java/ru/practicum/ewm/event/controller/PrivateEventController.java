package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.dto.UpdateEventRequest.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.PrivateEventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.PrivateRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    private final PrivateRequestService privateRequestService;

    @GetMapping
    public List<EventShortDto> getEvents(
            @PathVariable @Positive @NotNull Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("PrivateEventController - GET: /users/{}/events from={}, size={}", userId, from, size);
        return privateEventService.getEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
            @PathVariable @Positive @NotNull Long userId,
            @RequestBody @NotNull @Valid NewEventDto newEventDto) {
        log.info("PrivateEventController - POST: /users/{}/events newEventDto={}", userId, newEventDto);
        return privateEventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long eventId) {
        log.info("PrivateEventController - GET: /users/{}/events/{}", userId, eventId);
        return privateEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long eventId,
            @RequestBody @NotNull @Valid UpdateEventUserRequest updateEventRequest) {
        log.info("PrivateEventController - PATCH: /users/{}/events/{} updateEventRequest={}",
                userId, eventId, updateEventRequest);
        return privateEventService.updateEvent(userId, eventId, updateEventRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long eventId) {
        log.info("PrivateEventController - GET: /users/{}/events/{}/requests",
                userId, eventId);
        return privateRequestService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatusOfParticipationRequest(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long eventId,
            @RequestBody(required = false) @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PrivateEventController - PATCH: /users/{}/events/{}/requests eventRequestStatusUpdateRequest={}",
                userId, eventId, eventRequestStatusUpdateRequest);
        return privateRequestService.changeStatusOfParticipationRequest(
                userId,
                eventId,
                eventRequestStatusUpdateRequest);
    }
}
