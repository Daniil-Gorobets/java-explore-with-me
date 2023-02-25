package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventSortType;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventConverter;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;
import ru.practicum.ewm.util.stats.AppNamesConstants;
import ru.practicum.ewm.util.stats.HitConverter;
import ru.practicum.ewm.util.time.converter.TimeConverter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicEventService {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final StatsService statsService;

    private final CategoryRepository categoryRepository;

    public List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            HttpServletRequest request
    ) {
        LocalDateTime rangeStartTime;
        LocalDateTime rangeEndTime;
        if (rangeStart == null || rangeEnd == null) {
            rangeStartTime = LocalDateTime.now();
            rangeEndTime = TimeConverter.MAX_TIME;
        } else {
            rangeStartTime = TimeConverter.toTime(rangeStart);
            rangeEndTime = TimeConverter.toTime(rangeEnd);
        }
        if (text != null) {
            text = text.toLowerCase();
        }

        Sort defaultSort = Sort.by(Sort.Direction.ASC, "eventDate");
        PageRequest pageRequest = PageRequest.of(from / size, size, defaultSort);

        List<Event> events = eventRepository.findByParams(
                text,
                null,
                List.of(EventState.PUBLISHED),
                categories,
                paid,
                rangeStartTime,
                rangeEndTime,
                pageRequest
        );

        List<EventFullDto> eventFullDtos = EventConverter.toEventFullDtoListWithRequestsAndViews(
                events,
                requestRepository,
                statsService
        );

        if (onlyAvailable) {
            eventFullDtos = eventFullDtos.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        if (sort != null && sort.equals(EventSortType.VIEWS.toString())) {
            eventFullDtos.sort((e1, e2) -> e2.getViews().compareTo(e1.getViews()));
        }

        statsService.createView(HitConverter.toEndpointHit(AppNamesConstants.MAIN_APP_NAME, request));

        return eventFullDtos.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " not found"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Event with id=" + id + " not found");
        }

        statsService.createView(HitConverter.toEndpointHit(AppNamesConstants.MAIN_APP_NAME, request));
        return EventConverter.toEventFullDtoListWithRequestsAndViews(
                List.of(event),
                requestRepository,
                statsService).get(0);
    }
}
