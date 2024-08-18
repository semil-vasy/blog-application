package com.example.blog.service;

import com.example.blog.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getCommentByPostId(long postId);

    CommentDto createComment(CommentDto commentDto, long userId, long postId);

    void deleteComment(long commentId);
}
