package com.myblog.myblog.service.impl;

import com.myblog.myblog.entity.Post;
import com.myblog.myblog.payload.PostDto;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse  {
    private List<PostDto> content;
    private int pageNo;
    private int pageSize;
    private  int totalElements;
    private int totalPages;
    private boolean isLast;

}
