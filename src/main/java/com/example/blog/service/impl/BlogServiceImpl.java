package com.example.blog.service.impl;

import com.example.blog.dto.blog.BlogCustomDTO;
import com.example.blog.dto.blog.BlogDTO;
import com.example.blog.dto.blog.TagDTO;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Blog;
import com.example.blog.model.User;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.TagRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.BlogService;
import com.example.blog.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TagRepository tagRepository;

    public BlogServiceImpl(BlogRepository blogRepository, UserRepository userRepository, ModelMapper modelMapper, UserService userService, TagRepository tagRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.tagRepository = tagRepository;
    }


    @Override
    public BlogDTO createBlog(BlogDTO blogDto) {
        User user = userService.getAuthUser();
        blogDto.setTags(saveTags(blogDto.getTags()));
        Blog blog = this.modelMapper.map(blogDto, Blog.class);
        blog.setUser(user);
        blog.setBlogId(UUID.randomUUID().toString());
        Blog savedBlog = this.blogRepository.save(blog);
        return this.modelMapper.map(savedBlog, BlogDTO.class);
    }

    @Override
    public BlogDTO updateBlog(BlogDTO blogDto, long blogId) {
        Blog blog = this.blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Blog", "blog id", blogId));
//        blog.setBlogTitle(blogDto.getPostTitle());
//        blog.setPostContent(blogDto.getPostContent());
//        blog.setPostImage(blogDto.getPostImage());

        Blog savedBlog = this.blogRepository.save(blog);

        return this.modelMapper.map(savedBlog, BlogDTO.class);
    }

    @Override
    public void deleteBlog(long blogId) {
        Blog blog = this.blogRepository.findById(blogId).orElseThrow(() -> new ResourceNotFoundException("Blog", "blog id", blogId));
        this.blogRepository.delete(blog);
    }

    @Override
    public List<BlogCustomDTO> getAllBlog(long userId, int offset, int limit, String tagNames) {
        return this.blogRepository.getAllBlogs(userId, offset, limit, tagNames);
    }

    @Override
    public List<BlogCustomDTO> getTrendingBlogs() {
        return this.blogRepository.getTrendingBlogs();
    }

    @Override
    public BlogCustomDTO getBlogByBlogId(String blogId) {
        BlogCustomDTO blogCustomDTO = this.blogRepository.getBlogByBlogId(blogId);
        if (blogCustomDTO == null) {
            throw new ResourceNotFoundException("Blog", "blog id", blogId);
        }
        return blogCustomDTO;
    }

    @Override
    public List<BlogDTO> searchTitle(String keyword) {
        List<Blog> blogs = this.blogRepository.findByBlogTitleLike("%" + keyword.toLowerCase() + "%");
        return blogs.stream().map(blog -> this.modelMapper.map(blog, BlogDTO.class)).toList();
    }

    public Set<TagDTO> saveTags(Set<TagDTO> tagDTOs) {
        // Fetch all existing tags from the database that match the names in the incoming tags
        Set<String> tagNames = tagDTOs.stream().map(TagDTO::getTagName).collect(Collectors.toSet());
        Set<TagDTO> existingTags = new HashSet<>(tagRepository.findByNameIn(tagNames));

        // Convert TagDTOs to Tags, skipping already existing tags
        Set<TagDTO> newTags = tagDTOs.stream()
                .filter(tagDTO -> existingTags.stream()
                        .noneMatch(tag -> tag.getTagName().equals(tagDTO.getTagName())))
                .map(tagDTO -> new TagDTO(tagDTO.getTagName()))
                .collect(Collectors.toSet());

        // Combine the new tags with the existing ones
        existingTags.addAll(newTags);

        return existingTags; // Return the combined set of tags (new + existing)
    }
}
