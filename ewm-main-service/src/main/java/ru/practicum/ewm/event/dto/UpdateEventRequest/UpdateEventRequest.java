package ru.practicum.ewm.event.dto.UpdateEventRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.util.time.validator.ValidLocalDateTime;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    Long category;
    @Size(min = 20, max = 7000)
    String description;
    @ValidLocalDateTime
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    @Size(min = 3, max = 120)
    String title;
}
