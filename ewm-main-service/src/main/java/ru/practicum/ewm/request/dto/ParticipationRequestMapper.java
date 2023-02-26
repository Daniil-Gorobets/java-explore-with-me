package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.util.time.converter.TimeConverter;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(TimeConverter.toString(participationRequest.getCreated()))
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus().toString())
                .build();
    }
}
