package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.StateActionAdmin;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventConverter;
import ru.practicum.ewm.exception.IntegrityException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;
import ru.practicum.ewm.util.time.converter.TimeConverter;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final StatsService statsService;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    public List<EventFullDto> getEvents(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size) {
        LocalDateTime rangeStartTime;
        LocalDateTime rangeEndTime;
        if (rangeStart == null || rangeEnd == null) {
            rangeStartTime = LocalDateTime.now();
            rangeEndTime = TimeConverter.MAX_TIME;
        } else {
            rangeStartTime = TimeConverter.toTime(rangeStart);
            rangeEndTime = TimeConverter.toTime(rangeEnd);
        }


        Sort defaultSort = Sort.by(Sort.Direction.ASC, "eventDate");
        PageRequest pageRequest = PageRequest.of(from / size, size, defaultSort);
        List<Event> events = eventRepository.findByParams(
                null,
                users,
                states,
                categories,
                null,
                rangeStartTime,
                rangeEndTime,
                pageRequest);

        return EventConverter.toEventFullDtoListWithRequestsAndViews(
                events,
                requestRepository,
                statsService);
    }

    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));

        if (updateEventRequest.getStateAction() == StateActionAdmin.PUBLISH_EVENT
                && event.getState() != EventState.PENDING) {
            throw new IntegrityException("Event can be published only if current event status is PENDING. " +
                    "Current event status: " +  event.getState());
        }
        if (updateEventRequest.getStateAction() == StateActionAdmin.REJECT_EVENT
                && event.getState() == EventState.PUBLISHED) {
            throw new IntegrityException("Event can be rejected only if it is not published");
        }
        if (updateEventRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventRequest.getLocation());
            updateEventRequest.setLocation(location);
        }

        Event updatedEvent = EventConverter.toAdminUpdatedEvent(event, updateEventRequest, categoryRepository);
        updatedEvent = eventRepository.save(updatedEvent);

        return  EventConverter.toEventFullDtoListWithRequestsAndViews(
                List.of(updatedEvent),
                requestRepository,
                statsService).get(0);
    }
}
