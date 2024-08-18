package com.example.blog.service;

import com.example.blog.dto.blog.PostDto;
import com.example.blog.dto.blog.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto, long userId, long categoryId);

    PostDto updatePost(PostDto postDto, long postId);

    void deletePost(long postId);

    PostResponse getAllPost(int pageSize, int pageNumber, String sortBy, String sortDir);

    PostDto getPostById(long postId);

    List<PostDto> getPostByUser(long userId);

    List<PostDto> getPostByCategory(long categoryId);

    List<PostDto> searchTitle(String keyword);


}
