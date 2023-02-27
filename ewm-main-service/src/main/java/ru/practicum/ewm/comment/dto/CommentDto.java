package ru.practicum.ewm.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    String created;
    String text;
    Long user;
    Long event;
    String state;
}
