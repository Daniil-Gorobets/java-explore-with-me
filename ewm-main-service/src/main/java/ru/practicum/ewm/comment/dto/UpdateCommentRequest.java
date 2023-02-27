package ru.practicum.ewm.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.comment.model.UpdateCommentRequestStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCommentRequest {
    String text;
    UpdateCommentRequestStatus status;
}
