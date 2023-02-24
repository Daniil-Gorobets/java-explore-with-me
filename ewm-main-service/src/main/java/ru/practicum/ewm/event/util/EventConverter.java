package ru.practicum.ewm.event.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.UpdateEventRequest.UpdateEventAdminRequest;
import ru.practicum.ewm.event.dto.UpdateEventRequest.UpdateEventRequest;
import ru.practicum.ewm.event.dto.UpdateEventRequest.UpdateEventUserRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.StateActionAdmin;
import ru.practicum.ewm.event.model.StateActionUser;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;
import ru.practicum.ewm.util.time.converter.TimeConverter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EventConverter {


    public static Event toAdminUpdatedEvent(
            Event event,
            UpdateEventAdminRequest updateEventAdminRequest,
            CategoryRepository categoryRepository) {
        EventState newState = event.getState();
        if (updateEventAdminRequest.getStateAction() == StateActionAdmin.PUBLISH_EVENT) {
            newState = EventState.PUBLISHED;
        }
        if (updateEventAdminRequest.getStateAction() == StateActionAdmin.REJECT_EVENT) {
            newState = EventState.CANCELED;
        }
        return updateEvent(event, updateEventAdminRequest, categoryRepository, newState);
    }

    public static Event toUserUpdatedEvent(
            Event event,
            UpdateEventUserRequest updateEventUserRequest,
            CategoryRepository categoryRepository) {
        EventState newState = event.getState();
        if (updateEventUserRequest.getStateAction() == StateActionUser.CANCEL_REVIEW) {
            newState = EventState.CANCELED;
        }
        if (updateEventUserRequest.getStateAction() == StateActionUser.SEND_TO_REVIEW) {
            newState = EventState.PENDING;
        }
        return updateEvent(event, updateEventUserRequest, categoryRepository, newState);
    }

    public static Event updateEvent(
            Event event,
            UpdateEventRequest updateEventRequest,
            CategoryRepository categoryRepository,
            EventState newState) {
        return Event.builder()
                .id(event.getId())
                .annotation(updateEventRequest.getAnnotation() != null ?
                        updateEventRequest.getAnnotation() : event.getAnnotation())
                .category(updateEventRequest.getCategory() != null ?
                        categoryRepository.findById(updateEventRequest.getCategory())
                                .orElseThrow(() -> new NotFoundException(
                                        "Category with id=" + updateEventRequest.getCategory() + " not found"))
                        : event.getCategory())
                .createdOn(event.getCreatedOn())
                .description(updateEventRequest.getDescription() != null ?
                        updateEventRequest.getDescription() : event.getDescription())
                .eventDate(updateEventRequest.getEventDate() != null ?
                        TimeConverter.toTime(updateEventRequest.getEventDate()) : event.getEventDate())
                .initiator(event.getInitiator())
                .location(updateEventRequest.getLocation() != null ?
                        updateEventRequest.getLocation() : event.getLocation())
                .paid(updateEventRequest.getPaid() != null ?
                        updateEventRequest.getPaid() : event.getPaid())
                .participantLimit(updateEventRequest.getParticipantLimit() != null ?
                        updateEventRequest.getParticipantLimit() : event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(updateEventRequest.getRequestModeration() != null ?
                        updateEventRequest.getRequestModeration() : event.getRequestModeration())
                .state(updateEventRequest instanceof UpdateEventAdminRequest ?
                        ((UpdateEventAdminRequest) updateEventRequest).getStateAction() != null ?
                                newState : event.getState()
                        : ((UpdateEventUserRequest) updateEventRequest).getStateAction() != null ?
                        newState : event.getState())
                .title(updateEventRequest.getTitle() != null ?
                        updateEventRequest.getTitle() : event.getTitle())
                .build();
    }

    public static List<EventFullDto> toEventFullDtoListWithRequestsAndViews(
            List<Event> events,
            RequestRepository requestRepository,
            StatsService statsService) {
        log.info("EventConverter - method call 'toEventFullDtoListWithRequestsAndViews' with params: events={} " +
                "requestRepository={}, statsService={}", events, requestRepository, statsService);

        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        log.info("EventConverter - method call 'toEventFullDtoListWithRequestsAndViews' uris={}", uris);
        Map<Long, Long> eventsIdsWithParticipationRequests = requestRepository
                .findParticipationRequestsWithStatNumberForEvents(
                        events.stream()
                                .map(Event::getId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        arr -> (Long) arr[0],
                        arr -> (Long) arr[1]));
        log.info("EventConverter - method call 'toEventFullDtoListWithRequestsAndViews' " +
                "eventsIdsWithParticipationRequests={}", eventsIdsWithParticipationRequests);
        Object responseBody = statsService.getViewStats(
                TimeConverter.toString(TimeConverter.MIN_TIME),
                TimeConverter.toString(TimeConverter.MAX_TIME),
                uris,
                false).getBody();
        log.info("EventConverter - method call 'toEventFullDtoListWithRequestsAndViews' responseBody={}", responseBody);
        List<ViewStatsDto> viewStatsDtos = new ObjectMapper().convertValue(responseBody, new TypeReference<>() {});
        log.info("EventConverter - method call 'toEventFullDtoListWithRequestsAndViews' viewStatsDtos={}", viewStatsDtos);

        if (viewStatsDtos.equals(Collections.emptyList())) {
            return events.stream()
                    .map(event -> {
                        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
                        eventFullDto.setConfirmedRequests(
                                eventsIdsWithParticipationRequests.get(eventFullDto.getId()).intValue());
                        eventFullDto.setViews(0L);
                        return eventFullDto;
                    })
                    .collect(Collectors.toList());
        } else {
            Map<Long, Long> eventsIdsWithViews = viewStatsDtos.stream()
                    .collect(Collectors.toMap(
                            viewStatsDto -> Long.parseLong(
                                    viewStatsDto
                                            .getUri()
                                            .split("/")[viewStatsDto.getUri()
                                            .split("/").length - 1]),
                            ViewStatsDto::getHits
                    ));
            log.info("EventConverter - method call 'toEventFullDtoListWithRequestsAndViews' eventsIdsWithViews={}",
                    eventsIdsWithViews);

            return events.stream()
                    .map(event -> {
                        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
                        eventFullDto.setConfirmedRequests(
                                eventsIdsWithParticipationRequests.get(eventFullDto.getId()).intValue());
                        eventFullDto.setViews(eventsIdsWithViews.get(eventFullDto.getId()));
                        return eventFullDto;
                    })
                    .collect(Collectors.toList());
        }
    }
}
