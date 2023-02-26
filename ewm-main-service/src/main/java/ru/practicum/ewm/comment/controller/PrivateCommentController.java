package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentStatusUpdateByInitiatorRequest;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentRequest;
import ru.practicum.ewm.comment.service.PrivateCommentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateCommentController {

    private final PrivateCommentService privateCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable @Positive @NotNull Long userId,
            @RequestParam @Positive @NotNull Long eventId,
            @RequestBody @NotNull @Valid NewCommentDto newCommentDto) {
        log.info("PrivateCommentController - POST: /users/{}/comments eventId={}, newCommentDto={}",
                userId, eventId, newCommentDto);
        return privateCommentService.createComment(userId, eventId, newCommentDto);
    }

    @PatchMapping(path = "/{commentId}")
    public CommentDto updateComment(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long commentId,
            @RequestBody @NotNull @Valid UpdateCommentRequest updateCommentRequest) {
        log.info("PrivateCommentController - PATCH: /users/{}/comments/{} updateCommentRequest={}",
                userId, commentId, updateCommentRequest);
        return privateCommentService.updateComment(userId, commentId, updateCommentRequest);
    }


    @DeleteMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long commentId) {
        log.info("PrivateCommentController - DELETE: /users/{}/comments/{}", userId, commentId);
        privateCommentService.deleteComment(userId, commentId);
    }

    @PostMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CommentDto> changeCommentsStatusByEventInitiator(
            @PathVariable @Positive @NotNull Long userId,
            @PathVariable @Positive @NotNull Long eventId,
            @RequestBody @Valid CommentStatusUpdateByInitiatorRequest commentStatusUpdateByInitiatorRequest) {
        log.info("PrivateCommentController - POST: /users/{}/comments/{}, commentStatusUpdateRequest={}",
                userId, eventId, commentStatusUpdateByInitiatorRequest);
        return privateCommentService.changeCommentsStatusByEventInitiator(
                userId,
                eventId,
                commentStatusUpdateByInitiatorRequest);
    }
}
