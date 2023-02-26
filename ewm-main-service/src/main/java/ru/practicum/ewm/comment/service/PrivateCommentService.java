package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.CommentState;
import ru.practicum.ewm.comment.model.UpdateByInitiatorCommentRequestStatus;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.IntegrityException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
        Comment newComment = CommentMapper.toComment(newCommentDto, user, event);
        if (!event.getRequestModeration()) {
            newComment.setState(CommentState.PUBLISHED);
        }

        return CommentMapper.toCommentDto(commentRepository.save(newComment));
    }

    public CommentDto updateComment(
            Long userId,
            Long commentId,
            UpdateCommentRequest updateCommentRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new IntegrityException("User is not a comment owner");
        }

        CommentState newState = comment.getState();
        if (updateCommentRequest.getStatus() != null) {
            newState = updateCommentStatus(updateCommentRequest, comment);
        }

        Comment updatedComment = Comment.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .text(updateCommentRequest.getText() != null ?
                        updateCommentRequest.getText() : comment.getText())
                .user(user)
                .event(comment.getEvent())
                .state(newState)
                .build();

        return CommentMapper.toCommentDto(commentRepository.save(updatedComment));
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        if (!userId.equals(comment.getUser().getId())) {
            throw new IntegrityException("User is not a comment owner");
        }
        commentRepository.deleteById(comment.getId());
    }

    public List<CommentDto> changeCommentsStatusByEventInitiator(
            Long userId,
            Long eventId,
            CommentStatusUpdateByInitiatorRequest commentStatusUpdateByInitiatorRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        List<Comment> comments = commentRepository.findAllByIdInAndEventId(
                commentStatusUpdateByInitiatorRequest.getCommentIds(), event.getId());
        boolean allCommentInRejectedOrPendingState = comments.stream()
                .allMatch(comment -> comment.getState() == CommentState.REJECTED
                        || comment.getState() == CommentState.PENDING);
        if (!allCommentInRejectedOrPendingState) {
            throw new IntegrityException("There is comments that are not in REJECTED or PENDING state");
        }
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new IntegrityException("User is not event initiator");
        }
        final CommentState newState;
        if (commentStatusUpdateByInitiatorRequest.getCommentStatus() ==
                UpdateByInitiatorCommentRequestStatus.APPROVE) {
            newState = CommentState.PUBLISHED;
        } else if (commentStatusUpdateByInitiatorRequest.getCommentStatus() ==
                UpdateByInitiatorCommentRequestStatus.REJECT) {
            newState = CommentState.REJECTED;
        } else {
            throw new IntegrityException("Unknown update comment request status: " +
                    commentStatusUpdateByInitiatorRequest.getCommentStatus() + ". Status should be APPROVE or REJECT");
        }
        List<Comment> commentsToChange = commentRepository.findAllByIdInAndEventId(
                commentStatusUpdateByInitiatorRequest.getCommentIds(),
                eventId);

        List<Comment> changedComments = commentsToChange.stream()
                .peek(comment -> comment.setState(newState))
                .collect(Collectors.toList());

        return commentRepository.saveAll(changedComments).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private CommentState updateCommentStatus(UpdateCommentRequest updateCommentRequest, Comment comment) {
        CommentState newState;
        switch (updateCommentRequest.getStatus()) {
            case PUBLISH:
                newState = handlePublishStatus(comment);
                break;
            case CANCEL:
                newState = handleCancelStatus(comment);
                break;
            default:
                throw new IntegrityException("Unknown update comment request status: " + updateCommentRequest.getStatus() +
                        ". Status should be CANCEL or PUBLISH.");
        }
        return newState;
    }

    private CommentState handlePublishStatus(Comment comment) {
        if (comment.getState() != CommentState.CANCELED) {
            throw new IntegrityException("Only comments in CANCELED state can be changed to PENDING. " +
                    "Current state: " + comment.getState());
        } else {
            return CommentState.PENDING;
        }
    }

    private CommentState handleCancelStatus(Comment comment) {
        if (comment.getState() != CommentState.PENDING) {
            throw new IntegrityException("Only comments in PENDING state can be changed to CANCELED. " +
                    "Current state: " + comment.getState());
        } else {
            return CommentState.CANCELED;
        }
    }


}
