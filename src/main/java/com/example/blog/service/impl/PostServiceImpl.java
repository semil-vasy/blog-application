package com.example.blog.service.impl;

import com.example.blog.dto.blog.PostDto;
import com.example.blog.dto.blog.PostResponse;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Blog;
import com.example.blog.model.Category;
import com.example.blog.model.User;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private BlogRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public PostDto createPost(PostDto postDto, long userId, long categoryId) {

        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));


        Blog blog = this.modelMapper.map(postDto, Blog.class);
//        blog.setPostImage("default.png");
//        blog.setCreatedAt(new Date());
        blog.setUser(user);
        blog.setCategory(category);

        Blog savedBlog = this.postRepository.save(blog);

        return this.modelMapper.map(savedBlog, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long postId) {
        Blog blog = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
//        blog.setBlogTitle(postDto.getPostTitle());
//        blog.setPostContent(postDto.getPostContent());
//        blog.setPostImage(postDto.getPostImage());

        Blog savedBlog = this.postRepository.save(blog);

        return this.modelMapper.map(savedBlog, PostDto.class);
    }

    @Override
    public void deletePost(long postId) {
        Blog blog = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
        this.postRepository.delete(blog);
    }

    @Override
    public PostResponse getAllPost(int pageSize, int pageNumber, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Blog> pagePost = this.postRepository.findAll(pageable);
        List<PostDto> postDtos = pagePost.getContent().stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setData(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPage(pagePost.getTotalPages());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long postId) {
        Blog blog = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
        return this.modelMapper.map(blog, PostDto.class);
    }

    @Override
    public List<PostDto> getPostByUser(long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
        List<Blog> blogs = this.postRepository.findByUser(user);
        return blogs.stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public List<PostDto> getPostByCategory(long categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
        List<Blog> blogs = this.postRepository.findByCategory(category);
        return blogs.stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public List<PostDto> searchTitle(String keyword) {
        List<Blog> blogs = this.postRepository.findByPostTitleLike("%" + keyword.toLowerCase() + "%");
        return blogs.stream().map(post -> this.modelMapper.map(post, PostDto.class)).toList();
    }
}
