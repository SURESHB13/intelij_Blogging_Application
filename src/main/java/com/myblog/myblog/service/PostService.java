package com.myblog.myblog.service;
import com.myblog.myblog.payload.PostDto;
import com.myblog.myblog.service.impl.PostResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDTO);
    List<PostDto> getAllPosts();

    PostDto getPostById(Long id);

    PostDto updatePost(PostDto postdto, Long id);

    void deletePostById(Long id);

    PostResponse getAllPostsThroughPaginationAndSorting(int pageNo, int pageSize, String sortBy, String sortDir);
}
