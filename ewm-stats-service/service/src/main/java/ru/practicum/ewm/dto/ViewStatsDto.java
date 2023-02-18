package ru.practicum.ewm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
