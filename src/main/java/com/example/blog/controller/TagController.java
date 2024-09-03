package com.example.blog.controller;

import com.example.blog.dto.blog.TagDTO;
import com.example.blog.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @PostMapping
    public TagDTO createTag(@RequestBody TagDTO tagDTO) {
        return tagService.createTag(tagDTO.getTagName());
    }

    @GetMapping("/latest")
    public List<TagDTO> getLatestTags() {
        return tagService.getLatestTag();
    }

}
