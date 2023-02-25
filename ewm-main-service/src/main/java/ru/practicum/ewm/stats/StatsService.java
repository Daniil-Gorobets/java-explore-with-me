package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.EndpointHitDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final StatsClient statsClient;

    public ResponseEntity<Object> getViewStats(
            String rangeStart,
            String rangeEnd,
            List<String> uris,
            Boolean unique) {
        log.info("StatsService - method call 'getViewStats' with params: rangeStart={}, rangeEnd={}, uris={}, " +
                        "unique={}", rangeStart, rangeEnd, uris, unique);
        return statsClient.getViewStats(rangeStart, rangeEnd, uris, unique);
    }

    public ResponseEntity<Object> createView(EndpointHitDto endpointHitDto) {
        log.info("StatsService - method call 'createView' with params: endpointHitDto={}", endpointHitDto);
        return statsClient.createEndpointHit(endpointHitDto);
    }

}
