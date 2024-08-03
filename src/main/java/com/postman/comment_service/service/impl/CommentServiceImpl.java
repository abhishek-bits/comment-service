package com.postman.comment_service.service.impl;

import com.postman.comment_service.dto.CommentDto;
import com.postman.comment_service.entity.Comment;
import com.postman.comment_service.repository.CommentRepository;
import com.postman.comment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<CommentDto> getAllCommentDtos() {
        List<Comment> comments = commentRepository.findAll();

        Map<Long, List<Comment>> commentIdToRepliesMap = new HashMap<>();

        Map<Long, CommentDto> commentIdToCommentDtosMap = new HashMap<>();

        Map<Long, Boolean> commentIdToVisitedMap = new HashMap<>();

        for(Comment comment : comments) {
            if(!commentIdToRepliesMap.containsKey(comment.getParentCommentId())) {
                commentIdToRepliesMap.put(comment.getParentCommentId(), new ArrayList<>());
            }
            commentIdToRepliesMap.get(comment.getParentCommentId()).add(comment);
            commentIdToCommentDtosMap.put(
                    comment.getId(),
                    CommentDto.builder()
                            .id(comment.getId())
                            .description(comment.getDescription())
                            .replies(new ArrayList<>())
                            .build());
            commentIdToVisitedMap.put(comment.getId(), Boolean.FALSE);
        }

        for(Map.Entry<Long, List<Comment>> entry : commentIdToRepliesMap.entrySet()) {
            List<CommentDto> commentsDtos = entry.getValue().stream()
                    .map(comment -> {
                        commentIdToVisitedMap.put(comment.getId(), Boolean.TRUE);
                        return CommentDto.builder()
                                .id(comment.getId())
                                .description(comment.getDescription())
                                .build();
                    })
                    .toList();
            commentIdToCommentDtosMap.get(entry.getKey()).getReplies().addAll(commentsDtos);
        }

        List<CommentDto> commentDtos = new ArrayList<>();

        for(Map.Entry<Long, CommentDto> entry : commentIdToCommentDtosMap.entrySet()) {
            if(!commentIdToVisitedMap.get(entry.getValue().getId())) {
                commentDtos.add(entry.getValue());
            }
        }

        return commentDtos;
    }

    @Override
    public CommentDto getCommentDtoById(Long id) {
        Optional<Comment> commentOptional = getCommentById(id);

        if(commentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }

        Comment comment = commentOptional.get();

        // Get all replies for this comment
        List<Comment> replies = commentRepository.findByParentCommentId(comment.getId());

        List<CommentDto> repliesCommentDto = new ArrayList<>();

        for(Comment reply : replies) {
            // Check if this reply has any further replies
            if(reply.getParentCommentId() == null) {
                repliesCommentDto.add(CommentDto.builder()
                        .id(reply.getId())
                        .description(reply.getDescription())
                        .replies(Collections.emptyList())
                        .build());
            } else {
                CommentDto replyCommentDto = getCommentDtoById(reply.getId());
                repliesCommentDto.add(replyCommentDto);
            }

        }

        return CommentDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .replies(repliesCommentDto)
                .build();
    }

    @Override
    public List<Comment> getAllComments() {
        // Dangerous -> Pagination
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getRepliesByCommentId(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if(commentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }

        return commentRepository.findByParentCommentId(commentId);

    }

    @Override
    public Comment saveComment(Comment comment) {
        Comment commentEntity = Comment.builder()
                .description(comment.getDescription())
                .likes(0)
                .build();
        if(comment.getParentCommentId() != null) {
            commentEntity.setParentCommentId(comment.getParentCommentId());
        }
        return commentRepository.save(commentEntity);
    }

    @Override
    public Comment updateComment(Comment comment) {
        return null;
    }

    @Override
    public Comment deleteCommentById(Long id) {
        return null;
    }

}
