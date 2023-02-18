package ru.practicum.ewm.dto;

import ru.practicum.ewm.model.EndpointHitModel;

public class EndpointHitMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHitModel endpointHitModel) {
        return EndpointHitDto.builder()
                .id(endpointHitModel.getId())
                .app(endpointHitModel.getApp())
                .uri(endpointHitModel.getUri())
                .ip(endpointHitModel.getIp())
                .timestamp(endpointHitModel.getTimestamp())
                .build();
    }

    public static EndpointHitModel toEndpointHitModel(EndpointHitDto endpointHitDto) {
        return EndpointHitModel.builder()
                .id(endpointHitDto.getId())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
