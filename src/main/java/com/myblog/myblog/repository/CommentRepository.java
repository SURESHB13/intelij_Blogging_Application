package com.myblog.myblog.repository;

import com.myblog.myblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long postId);
List<Comment>findByEmail(String email);
}
