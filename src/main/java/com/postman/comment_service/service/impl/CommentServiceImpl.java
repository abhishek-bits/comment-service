package com.postman.comment_service.service.impl;

import com.postman.comment_service.dto.CommentDto;
import com.postman.comment_service.entity.Comment;
import com.postman.comment_service.repository.CommentRepository;
import com.postman.comment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
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
    public CommentDto getCommentDtoById(Long id) {
        Optional<Comment> commentOptional = getCommentById(id);

        // All the replies to this comment will have refernce to the primary
        List<Comment> replies = commentRepository.findByParentCommentId(commentOptional.get().getId());

//        List<CommentDto> repliesDto = replies.stream().map(reply -> {
//            return CommentDto.builder()
//                    .id(reply.getId())
//                    .description(reply.getDescription())
//                    .build();
//        });
//
//        return CommentDto.builder().replies(replies).build();

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
                .parentCommentId(comment.getParentCommentId() != null ? comment.getParentCommentId() : 0)
                .build();
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
