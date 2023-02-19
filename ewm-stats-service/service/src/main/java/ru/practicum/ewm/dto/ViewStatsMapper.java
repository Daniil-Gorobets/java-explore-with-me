package ru.practicum.ewm.dto;

import ru.practicum.ewm.model.ViewStatsModel;

public class ViewStatsMapper {
    public static ViewStatsDto toViewStatsDto(ViewStatsModel viewStatsModel) {
        return ViewStatsDto.builder()
                .app(viewStatsModel.getApp())
                .uri(viewStatsModel.getUri())
                .hits(viewStatsModel.getHits())
                .build();
    }

    public static ViewStatsModel toViewStatsModel(ViewStatsDto viewStatsDto) {
        return ViewStatsModel.builder()
                .app(viewStatsDto.getApp())
                .uri(viewStatsDto.getUri())
                .hits(viewStatsDto.getHits())
                .build();
    }
}
