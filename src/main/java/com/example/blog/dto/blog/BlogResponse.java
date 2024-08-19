package com.example.blog.dto.blog;

import lombok.Data;

import java.util.List;

@Data
public class BlogResponse {

    private List<BlogDto> data;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPage;
    private boolean lastPage;
}
