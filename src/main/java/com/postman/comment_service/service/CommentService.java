package com.postman.comment_service.service;

import com.postman.comment_service.dto.CommentDto;
import com.postman.comment_service.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> getCommentById(Long id);

    CommentDto getCommentDtoById(Long id);

    List<Comment> getAllComments();

    List<Comment> getRepliesByCommentId(Long commentId);

    Comment saveComment(Comment comment);
    Comment updateComment(Comment comment);
    Comment deleteCommentById(Long id);


}
