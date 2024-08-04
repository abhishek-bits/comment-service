package com.postman.comment_service.repository;

import com.postman.comment_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByParentCommentId(Long commentId);

    List<Comment> findByParentCommentIdIsNull();

}
