package com.example.blog.service;

import com.example.blog.dto.blog.BlogDTO;
import com.example.blog.dto.blog.BlogResponse;

import java.util.List;

public interface BlogService {

    BlogDTO createBlog(BlogDTO blogDto);

    BlogDTO updateBlog(BlogDTO blogDto, long postId);

    void deleteBlog(long postId);

    BlogResponse getAllBlog(int pageSize, int pageNumber, String sortBy, String sortDir);

    BlogDTO getBlogById(long postId);

    List<BlogDTO> getBlogByUser(long userId);

    List<BlogDTO> searchTitle(String keyword);


}
