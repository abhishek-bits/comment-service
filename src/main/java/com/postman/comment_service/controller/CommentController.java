package com.postman.comment_service.controller;

import com.postman.comment_service.dto.ApiResponse;
import com.postman.comment_service.entity.Comment;
import com.postman.comment_service.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RequestMapping("/api/v1/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    ResponseEntity<ApiResponse> createComment(@RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Comment created successfully")
                        .data(Map.of("comment", commentService.saveComment(comment)))
                        .build());
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Comment fetched successfully")
                        .data(Map.of("comment", commentService.getCommentById(id)))
                        .build());
    }

    @GetMapping("/{id}/replies")
    ResponseEntity<ApiResponse> getRepliesByCommentId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .timeStamp(LocalDateTime.now())
                .message("Replies fetched successfully")
                .data(Map.of("replies", commentService.getRepliesByCommentId(id)))
                .build());
    }

    @GetMapping("/{id}/list")
    ResponseEntity<ApiResponse> getCommentDto(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .timeStamp(LocalDateTime.now())
                .message("Contacts fetched successfully")
                .data(Map.of("replies", commentService.getCommentDtoById(id)))
                .build());
    }
}
