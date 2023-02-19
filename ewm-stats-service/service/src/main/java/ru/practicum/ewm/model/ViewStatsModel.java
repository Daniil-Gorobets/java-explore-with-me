package ru.practicum.ewm.model;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStatsModel {
    String app;
    String uri;
    Long hits;
}
