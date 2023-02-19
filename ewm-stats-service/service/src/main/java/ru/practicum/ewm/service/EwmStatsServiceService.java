package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.EndpointHitMapper;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewStatsMapper;
import ru.practicum.ewm.model.EndpointHitModel;
import ru.practicum.ewm.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class EwmStatsServiceService {

    @Autowired
    private final EndpointHitRepository endpointHitRepository;

    public EndpointHitDto createEndpointHit(EndpointHitDto endpointHitDto) {
        log.debug("EwmStatsServiceService - method call 'createEndpointHit' with params: endpointHitDto={}",
                endpointHitDto);
        EndpointHitModel endpointHitModel = EndpointHitMapper.toEndpointHitModel(endpointHitDto);
        log.info(endpointHitModel.toString());
        return EndpointHitMapper.toEndpointHitDto(
                endpointHitRepository.save(
                        endpointHitModel));
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.debug("EwmStatsServiceService - method call 'getViewStats' with params: start={}, end={}, uris={}, " +
                "unique={}", start, end, uris, unique);
        if (unique) {
            return endpointHitRepository.findViewStatsUniqueIp(uris, start, end)
                    .stream()
                    .map(ViewStatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        } else {
            return endpointHitRepository.findViewStats(uris, start, end)
                    .stream()
                    .map(ViewStatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        }
    }

}
