package ru.practicum.ewm.event.dto.UpdateEventRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.StateActionUser;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(builderMethodName = "adminBuilder")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequest extends UpdateEventRequest {
    StateActionUser stateAction;
}
