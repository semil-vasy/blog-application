package com.example.blog.service.impl;

import com.example.blog.dto.comment.CommentDto;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Blog;
import com.example.blog.model.Comment;
import com.example.blog.model.User;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        Blog blog = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
        List<Comment> comments = this.commentRepository.findByBlog(blog);
        return comments.stream().map(comment -> this.modelMapper.map(comment, CommentDto.class)).toList();
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long userId, long postId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
        Blog blog = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setBlog(blog);
        comment.setCommentedBy(user);
        Comment savedComment = this.commentRepository.save(comment);

        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(long commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commentId));
        this.commentRepository.delete(comment);
    }
}
