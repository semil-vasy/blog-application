package com.example.blog.service;

import com.example.blog.dto.blog.BlogDto;
import com.example.blog.dto.blog.BlogResponse;

import java.util.List;

public interface BlogService {

    BlogDto createBlog(BlogDto blogDto, long userId, long categoryId);

    BlogDto updateBlog(BlogDto blogDto, long postId);

    void deleteBlog(long postId);

    BlogResponse getAllBlog(int pageSize, int pageNumber, String sortBy, String sortDir);

    BlogDto getBlogById(long postId);

    List<BlogDto> getBlogByUser(long userId);

    List<BlogDto> searchTitle(String keyword);


}
