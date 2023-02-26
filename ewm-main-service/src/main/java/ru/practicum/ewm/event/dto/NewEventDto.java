package ru.practicum.ewm.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.util.time.validator.ValidLocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    @PositiveOrZero
    Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @NotBlank
    @ValidLocalDateTime
    String eventDate;
    @NotNull
    Location location;
    Boolean paid = false;
    @PositiveOrZero
    Integer participantLimit = 0;
    Boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
