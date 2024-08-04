package com.postman.comment_service.service.impl;

import com.postman.comment_service.dto.CommentDto;
import com.postman.comment_service.entity.Comment;
import com.postman.comment_service.repository.CommentRepository;
import com.postman.comment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    public List<CommentDto> getAllComments() {
        return commentRepository.findByParentCommentIdIsNull()
                .stream()
                .map(this::getCommentDto)
                .toList();
    }

    private CommentDto getCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .likes(comment.getLikes())
                .replies(commentRepository.findByParentCommentId(comment.getId())
                        .stream()
                        .map(this::getCommentDto)
                        .toList())
                .build();
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
    public Comment updateCommentById(Long id, Comment commentRequest) {

        Optional<Comment> commentOptional = commentRepository.findById(id);

        if(commentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found.");
        }

        Comment comment = commentOptional.get();

        comment.setDescription(commentRequest.getDescription());
        comment.setLikes(commentRequest.getLikes());

        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment deleteCommentById(Long id) {

        Optional<Comment> commentOptional = commentRepository.findById(id);

        if(commentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found.");
        }

        deleteComment(commentOptional.get());

        return commentOptional.get();
    }

    private void deleteComment(Comment comment) {
        List<Comment> replies = commentRepository.findByParentCommentId(comment.getId());
        replies.forEach(this::deleteComment);
        commentRepository.deleteAll(replies);
        commentRepository.delete(comment);
    }

}
