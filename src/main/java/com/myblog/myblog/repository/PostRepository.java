package com.myblog.myblog.repository;

import com.myblog.myblog.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByTitleContainingOrDescriptionContainingOrContentContaining(String title, String description, String content);

}
