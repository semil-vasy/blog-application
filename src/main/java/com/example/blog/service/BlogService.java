package com.example.blog.service;

import com.example.blog.dto.blog.BlogCustomDTO;
import com.example.blog.dto.blog.BlogDTO;

import java.util.List;

public interface BlogService {

    List<BlogCustomDTO> getAllBlog(long userId, int offset, int limit, String tagNames);

    List<BlogCustomDTO> getTrendingBlogs();

    BlogDTO createBlog(BlogDTO blogDto);

    BlogDTO updateBlog(BlogDTO blogDto, long postId);

    void deleteBlog(long postId);

    BlogCustomDTO getBlogByBlogId(String postId);

    List<BlogDTO> searchTitle(String keyword);


}
