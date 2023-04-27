package com.myblog.myblog.service.impl;

import com.myblog.myblog.entity.Post;
import com.myblog.myblog.exception.ResourceNotFoundException;
import com.myblog.myblog.payload.PostDto;
import com.myblog.myblog.repository.PostRepository;
import com.myblog.myblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(@RequestBody PostDto postDTO) {
        Post post=new Post();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        Post newPost = postRepository.save(post);

        PostDto dto=new PostDto();
        dto.setId(newPost.getId());
        dto.setTitle(newPost.getTitle());
        dto.setDescription(newPost.getDescription());
        dto.setContent(newPost.getContent());
        return dto;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> all = postRepository.findAll();
        return all.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    }



    @Override
    public PostDto getPostById(Long id) {
       Post post=postRepository.findById(id).orElseThrow(
               ()->new ResourceNotFoundException("post","id",id)
       );
        PostDto postDto = mapToDTO(post);
        return  postDto;
    }

    @Override
    public PostDto updatePost(PostDto postdto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post","ID",id)
        );
        post.setContent(postdto.getContent());
        post.setDescription(postdto.getDescription());
        post.setTitle(postdto.getTitle());
        Post updatedPost = postRepository.save(post);
        return  mapToDTO(updatedPost);



    }

    @Override
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", id)
        );
        postRepository.deleteById(id);
    }


    @Override
    public PostResponse getAllPostsThroughPaginationAndSorting(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNo, pageSize,sort);
        Page<Post> content = postRepository.findAll(pageable);
        List<Post> posts =content.getContent();
        List<PostDto> dto = posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(dto);
        postResponse.setTotalElements((int)content.getTotalElements());
        postResponse.setPageNo(content.getNumber());
        postResponse.setPageSize(content.getSize());
        postResponse.setTotalPages(content.getTotalPages());
        postResponse.setLast(content.isLast());

        return postResponse;


    }

    @Override
    public List<PostDto> getPostsBySearch(String keyword) {
        List<Post> posts= postRepository.findByTitleContainingOrDescriptionContainingOrContentContaining(keyword, keyword, keyword);
    return posts.stream().map(post ->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());}


    private PostDto mapToDTO(Post post){
        PostDto dto = modelMapper.map(post, PostDto.class);
        return dto;
    }


}
