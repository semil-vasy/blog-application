package com.example.blog.dto.blog;

public interface BlogCustomDTO {
    long getId();

    String getBlogId();

    String getTitle();

    String getContent();

    String getDescription();

    String getBanner();

    String getCreatedOn();

    String getUsername();

    String getProfileImage();

    String getTagNames();

    Integer getTotalLikes();

    Integer getTotalReads();
}
