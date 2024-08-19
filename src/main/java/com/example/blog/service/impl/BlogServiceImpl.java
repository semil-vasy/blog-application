package com.example.blog.service.impl;

import com.example.blog.dto.blog.BlogDto;
import com.example.blog.dto.blog.BlogResponse;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Blog;
import com.example.blog.model.User;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.BlogService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public BlogServiceImpl(BlogRepository blogRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public BlogDto createBlog(BlogDto blogDto, long userId, long categoryId) {

        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));


        Blog blog = this.modelMapper.map(blogDto, Blog.class);
//        blog.setPostImage("default.png");
//        blog.setCreatedAt(new Date());
        blog.setUser(user);

        Blog savedBlog = this.blogRepository.save(blog);

        return this.modelMapper.map(savedBlog, BlogDto.class);
    }

    @Override
    public BlogDto updateBlog(BlogDto blogDto, long blogId) {
        Blog blog = this.blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Blog", "blog id", blogId));
//        blog.setBlogTitle(blogDto.getPostTitle());
//        blog.setPostContent(blogDto.getPostContent());
//        blog.setPostImage(blogDto.getPostImage());

        Blog savedBlog = this.blogRepository.save(blog);

        return this.modelMapper.map(savedBlog, BlogDto.class);
    }

    @Override
    public void deleteBlog(long blogId) {
        Blog blog = this.blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Blog", "blog id", blogId));
        this.blogRepository.delete(blog);
    }

    @Override
    public BlogResponse getAllBlog(int pageSize, int pageNumber, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Blog> pageBlog = this.blogRepository.findAll(pageable);
        List<BlogDto> blogDtos = pageBlog.getContent().stream().map(blog -> this.modelMapper.map(blog, BlogDto.class)).toList();

        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setData(blogDtos);
        blogResponse.setPageNumber(pageBlog.getNumber());
        blogResponse.setPageSize(pageBlog.getSize());
        blogResponse.setTotalPage(pageBlog.getTotalPages());
        blogResponse.setTotalElements(pageBlog.getTotalElements());
        blogResponse.setLastPage(pageBlog.isLast());

        return blogResponse;
    }

    @Override
    public BlogDto getBlogById(long blogId) {
        Blog blog = this.blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Blog", "blog id", blogId));
        return this.modelMapper.map(blog, BlogDto.class);
    }

    @Override
    public List<BlogDto> getBlogByUser(long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
        List<Blog> blogs = this.blogRepository.findByUser(user);
        return blogs.stream().map(blog -> this.modelMapper.map(blog, BlogDto.class)).toList();
    }

    @Override
    public List<BlogDto> searchTitle(String keyword) {
        List<Blog> blogs = this.blogRepository.findByBlogTitleLike("%" + keyword.toLowerCase() + "%");
        return blogs.stream().map(blog -> this.modelMapper.map(blog, BlogDto.class)).toList();
    }
}
