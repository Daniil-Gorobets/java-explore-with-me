package ru.practicum.ewm.model;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ViewStatsModel {
    private String app;
    private String uri;
    private Long hits;
}
