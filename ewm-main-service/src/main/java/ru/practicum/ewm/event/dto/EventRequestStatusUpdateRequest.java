package ru.practicum.ewm.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestStatus requestStatus;
}
