package ru.practicum.ewm.compilation.utils;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.util.EventConverter;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationConverter {
    public static CompilationDto compilationToCompilationDto(
            Compilation compilation,
            RequestRepository requestRepository,
            StatsService statsService) {

        List<EventShortDto> eventShortDtos = EventConverter.toEventFullDtoListWithRequestsAndViews(
                        compilation.getEvents(),
                        requestRepository,
                        statsService).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        return CompilationDto.builder()
                .id(compilation.getId())
                .events(eventShortDtos)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
