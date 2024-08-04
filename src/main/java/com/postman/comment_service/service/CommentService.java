package com.postman.comment_service.service;

import com.postman.comment_service.dto.CommentDto;
import com.postman.comment_service.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> getCommentById(Long id);
    List<CommentDto> getAllComments();
    Comment saveComment(Comment comment);
    Comment updateCommentById(Long id, Comment comment);
    Comment deleteCommentById(Long id);


}
