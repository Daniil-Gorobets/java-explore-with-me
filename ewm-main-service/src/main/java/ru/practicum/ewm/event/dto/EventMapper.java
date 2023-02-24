package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.util.time.converter.TimeConverter;

import java.time.LocalDateTime;

public class EventMapper {

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(null)
                .createdOn(TimeConverter.toString(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(TimeConverter.toString(event.getEventDate()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(TimeConverter.toString(event.getPublishedOn()))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(null)
                .build();
    }

    public static EventShortDto toEventShortDto(EventFullDto eventFullDto) {
        return EventShortDto.builder()
                .id(eventFullDto.getId())
                .annotation(eventFullDto.getAnnotation())
                .category(eventFullDto.getCategory())
                .confirmedRequests(eventFullDto.getConfirmedRequests())
                .eventDate(eventFullDto.getEventDate())
                .initiator(eventFullDto.getInitiator())
                .paid(eventFullDto.getPaid())
                .title(eventFullDto.getTitle())
                .views(eventFullDto.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(null)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(TimeConverter.toTime(newEventDto.getEventDate()))
                .initiator(null)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }
}
