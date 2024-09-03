package com.example.blog.service;

import com.example.blog.dto.blog.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO createTag(String name);

    List<TagDTO> getLatestTag();


}
