package com.example.blog.dto.comment;

import com.example.blog.model.User;
import lombok.Data;

@Data
public class CommentDto {
    private long commentId;
    private String commentContent;
    private User user;
}
