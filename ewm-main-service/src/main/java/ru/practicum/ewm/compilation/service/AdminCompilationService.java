package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.EventConverter;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final StatsService statsService;

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events  = new ArrayList<>();
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        if (!newCompilationDto.getEvents().equals(Collections.emptyList())) {
            events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            List<EventFullDto> eventFullDtos = EventConverter.toEventFullDtoListWithRequestsAndViews(
                    events,
                    requestRepository,
                    statsService);
            eventShortDtos = eventFullDtos.stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events));
        return CompilationMapper.toCompilationDto(compilation, eventShortDtos);
    }

    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        compilationRepository.deleteById(compId);
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        List<Event> newEventList = compilation.getEvents();
        if (updateCompilationRequest.getEvents() != null) {
            newEventList = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
        }

        Compilation newCompilation = Compilation.builder()
                .id(compilation.getId())
                .events(newEventList)
                .pinned(updateCompilationRequest.getPinned() != null ?
                        updateCompilationRequest.getPinned() : compilation.getPinned())
                .title(updateCompilationRequest.getTitle() != null ?
                        updateCompilationRequest.getTitle() : compilation.getTitle())
                .build();

        List<EventShortDto> eventShortDtos = EventConverter.toEventFullDtoListWithRequestsAndViews(
                        newEventList,
                        requestRepository,
                        statsService).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        Compilation updatedCompilation = compilationRepository.save(newCompilation);
        return CompilationMapper.toCompilationDto(updatedCompilation, eventShortDtos);
    }
}
