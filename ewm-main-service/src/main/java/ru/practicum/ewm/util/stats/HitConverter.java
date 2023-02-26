package ru.practicum.ewm.util.stats;

import ru.practicum.ewm.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class HitConverter {
    public static EndpointHitDto toEndpointHit(String app, HttpServletRequest request) {
        return EndpointHitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
