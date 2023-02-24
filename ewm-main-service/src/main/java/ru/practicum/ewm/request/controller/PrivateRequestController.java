package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.PrivateRequestService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateRequestController {

    @Autowired
    private final PrivateRequestService privateRequestService;

    @GetMapping
    public List<ParticipationRequestDto> getUserParticipationRequests(
            @PathVariable @Positive @NotNull Long userId) {
        log.info("PrivateRequestController - GET: /users/{}/requests", userId);
        return privateRequestService.getUserParticipationRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequests(
            @PathVariable @Positive @NotNull Long userId,
            @RequestParam @Positive @NotNull Long eventId) {
        log.info("PrivateRequestController - POST: /users/{}/requests eventId={}", userId, eventId);
        return privateRequestService.createParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequests(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long requestId) {
        log.info("PrivateRequestController - POST: /users/{}/requests/{}/cancel", userId, requestId);
        return privateRequestService.cancelParticipationRequests(userId, requestId);
    }
}
