package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventConverter;
import ru.practicum.ewm.exception.IntegrityException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestService {

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final StatsService statsService;


    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {
        return requestRepository.findAllByEventIdAndEvent_InitiatorId(eventId, userId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult changeStatusOfParticipationRequest(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        EventFullDto eventFullDto = EventConverter.toEventFullDtoListWithRequestsAndViews(
                List.of(event),
                requestRepository,
                statsService).get(0);

        if (eventFullDto.getConfirmedRequests() >= eventFullDto.getParticipantLimit()) {
            throw new IntegrityException("The participant limit has been reached");
        }

        if (eventFullDto.getParticipantLimit() == 0 || !eventFullDto.getRequestModeration()) {
            throw new IntegrityException("No need to confirm request");
        }

        List<ParticipationRequest> participationRequests = requestRepository.findAllByIdIn(
                eventRequestStatusUpdateRequest.getRequestIds());

        if (!participationRequests.stream()
                .allMatch(participationRequest -> participationRequest.getStatus() == RequestStatus.PENDING)) {
            throw new IntegrityException("Request must have status PENDING");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (eventRequestStatusUpdateRequest.getRequestStatus() == RequestStatus.CONFIRMED) {
            int placesLeft = eventFullDto.getParticipantLimit() - eventFullDto.getConfirmedRequests();
            for (ParticipationRequest participationRequest : participationRequests) {
                if (placesLeft > 0) {
                    participationRequest.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
                    placesLeft--;
                } else {
                    participationRequest.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
                }
            }
        } else if (eventRequestStatusUpdateRequest.getRequestStatus() == RequestStatus.REJECTED) {
            participationRequests.forEach(participationRequest -> {
                participationRequest.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
            });
        } else {
            throw new IntegrityException("Incorrect status");
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    public List<ParticipationRequestDto> getUserParticipationRequests(Long userId) {
        List<ParticipationRequest> participationRequests = requestRepository.findAllByRequesterId(userId);
        return participationRequests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto createParticipationRequests(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new IntegrityException("No duplicate requests allowed");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new IntegrityException("Requests from initiator not allowed");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new IntegrityException("Requests only allowed to published events");
        }
        EventFullDto eventFullDto = EventConverter.toEventFullDtoListWithRequestsAndViews(
                List.of(event),
                requestRepository,
                statsService).get(0);
        if (eventFullDto.getConfirmedRequests() >= eventFullDto.getParticipantLimit()) {
            throw new IntegrityException("Participation request limit is reached");
        }

        ParticipationRequest participationRequest =
                ParticipationRequest.builder()
                        .created(LocalDateTime.now())
                        .event(event)
                        .requester(requester)
                        .status(RequestStatus.PENDING)
                        .build();

        if (!event.getRequestModeration()) {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
        }
        log.info("||||||||||||||||||||  participationRequest = {}", participationRequest);
        participationRequest = requestRepository.save(participationRequest);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
    }

    public ParticipationRequestDto cancelParticipationRequests(Long userId, Long requestId) {
        ParticipationRequest participationRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + userId + " was not found"));
        if (!userId.equals(participationRequest.getRequester().getId())) {
            throw new IntegrityException("User is not requestor");
        }
        participationRequest.setStatus(RequestStatus.REJECTED);
        participationRequest = requestRepository.save(participationRequest);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
    }
}
