package com.example.blog.dto.blog;

import com.example.blog.dto.category.CategoryDto;
import com.example.blog.dto.user.UserFormDto;
import lombok.Data;

import java.util.Date;

@Data
public class BlogDto {

    private long blogId;

    private String blogTitle;

    private String blogContent;

    private String blogImage;

    private Date createdAt;

    private UserFormDto user;

    private CategoryDto category;

//    private List<CommentDto> comments = new ArrayList<>();

}
