package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.comment.model.UpdateByInitiatorCommentRequestStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentStatusUpdateByInitiatorRequest {
    List<Long> commentIds;
    @JsonProperty("status")
    UpdateByInitiatorCommentRequestStatus commentStatus;
}
