package ru.practicum.ewm.event.dto.UpdateEventRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.StateActionAdmin;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(builderMethodName = "userBuilder")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest extends UpdateEventRequest {
    StateActionAdmin stateAction;
}
