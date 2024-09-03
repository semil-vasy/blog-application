package com.example.blog.service.impl;

import com.example.blog.dto.blog.TagDTO;
import com.example.blog.model.Tag;
import com.example.blog.repository.TagRepository;
import com.example.blog.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDTO createTag(String name) {
        return modelMapper.map(tagRepository.save(new Tag(0, name)), TagDTO.class);
    }

    @Override
    public List<TagDTO> getLatestTag() {
        List<Tag> latestTags = tagRepository.latestTags();
        return latestTags.stream().map(tag -> modelMapper.map(tag, TagDTO.class)).toList();
    }
}
