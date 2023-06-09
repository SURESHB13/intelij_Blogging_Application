package com.myblog.myblog.controller;

import com.myblog.myblog.payload.PostDto;
import com.myblog.myblog.service.PostService;
import com.myblog.myblog.service.impl.PostResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts/")
public class PostController {
    @Autowired
   private PostService postService;


//http:localhost:8090/api/posts
    public PostController(PostService postService) {
        this.postService = postService;
    }
//http://localhost:8090/api/posts
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> createPost(@Valid  @RequestBody PostDto postDTO ,BindingResult result) {

        if (result.hasErrors())
        {
            return  new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = postService.createPost(postDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public List<PostDto> getAllPosts(){
        return postService.getAllPosts();
    }

    //http://locahost:8090/api/posts/?pageNo=0&pageSize=5

    //http://locahost:8090/api/posts/?pageNo=0&pageSize=5&sortBy=id&sortDir=asc
    @GetMapping
    public ResponseEntity<PostResponse> getAllPostsThroughPaginationAndSorting(
            @RequestParam(value = "pageNo", defaultValue = "10", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ){
        PostResponse postResponse = postService.getAllPostsThroughPaginationAndSorting(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);

    }


    //localhost:8090/api/posts/1
    @GetMapping("/{id}")
    public ResponseEntity<PostDto>getPostById(@PathVariable("id")Long id){


       return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);


    }//http://localhost:8090/api/posts/2
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<PostDto>updatePost(@RequestBody PostDto postdto,@PathVariable ("id") Long id){
        PostDto postresponse = postService.updatePost(postdto, id);
        return  ResponseEntity.ok(postresponse);
        //another way
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
public  ResponseEntity<String>deletePostById(@PathVariable("id")Long id){
postService.deletePostById(id);
       return new ResponseEntity<>("Post deleted by Id successfully!!!!....",HttpStatus.OK);
    }


    @GetMapping("/search")
    public List<PostDto> getPostsBySearch(@RequestParam("keyword") String keyword){
        return postService.getPostsBySearch(keyword);}
}
