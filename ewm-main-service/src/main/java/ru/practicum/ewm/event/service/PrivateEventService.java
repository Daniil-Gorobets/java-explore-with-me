package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest.UpdateEventUserRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventConverter;
import ru.practicum.ewm.exception.IntegrityException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;
import ru.practicum.ewm.location.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateEventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final StatsService statsService;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final LocationRepository locationRepository;

    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
            Sort defaultSort = Sort.by(Sort.Direction.ASC, "eventDate");
            PageRequest pageRequest = PageRequest.of(from / size, size, defaultSort);
            List<Event> events = eventRepository.findByParams(
                    null,
                    List.of(userId),
                    null,
                    null,
                    null,
                    null,
                    null,
                    pageRequest);

            return EventConverter.toEventFullDtoListWithRequestsAndViews(
                    events, requestRepository, statsService).stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
    }

    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Location location = locationRepository.save(newEventDto.getLocation());
        newEventDto.setLocation(location);
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + newEventDto.getCategory() +
                        " not found")));
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found")));
        event = eventRepository.save(event);
        return EventConverter.toEventFullDtoListWithRequestsAndViews(
                List.of(event),
                requestRepository,
                statsService).get(0);
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " not found");
        }
        return EventConverter.toEventFullDtoListWithRequestsAndViews(
                List.of(event),
                requestRepository,
                statsService).get(0);
    }

    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " not found");
        }
        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            throw new IntegrityException("Only pending or canceled events can be changed");
        }

        Event updatedEvent = EventConverter.toUserUpdatedEvent(event, updateEventUserRequest, categoryRepository);
        updatedEvent = eventRepository.save(updatedEvent);
        log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        return EventConverter.toEventFullDtoListWithRequestsAndViews(
                List.of(updatedEvent),
                requestRepository,
                statsService).get(0);
    }
}
