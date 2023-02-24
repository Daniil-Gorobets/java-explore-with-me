package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.utils.CompilationConverter;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.stats.StatsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationService {

    @Autowired
    private final CompilationRepository compilationRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final StatsService statsService;


    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest);


        return compilations.stream()
                .map(compilation -> CompilationConverter.compilationToCompilationDto(
                        compilation,
                        requestRepository,
                        statsService))
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        return CompilationConverter.compilationToCompilationDto(
                compilation,
                requestRepository,
                statsService);
    }
}
