package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentMapper;
import ru.practicum.ewm.comment.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCommentService {

    private final CommentRepository commentRepository;

    public List<CommentDto> getEventComments(Long eventId) {
        return commentRepository.findAllByEventId(eventId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
