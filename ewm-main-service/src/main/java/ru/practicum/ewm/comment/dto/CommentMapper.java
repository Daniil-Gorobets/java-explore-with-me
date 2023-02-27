package ru.practicum.ewm.comment.dto;

import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.time.converter.TimeConverter;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(NewCommentDto newCommentDto, User user, Event event) {
        return Comment.builder()
                .created(LocalDateTime.now())
                .text(newCommentDto.getText())
                .user(user)
                .event(event)
                .state(CommentState.PENDING)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .created(TimeConverter.toString(comment.getCreated()))
                .text(comment.getText())
                .user(comment.getUser().getId())
                .event(comment.getEvent().getId())
                .state(comment.getState().toString())
                .build();
    }
}
