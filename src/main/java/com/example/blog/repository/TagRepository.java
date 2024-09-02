package com.example.blog.repository;

import com.example.blog.dto.blog.TagDTO;
import com.example.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select new com.example.blog.dto.blog.TagDTO(t.id, t.name) from Tag t where t.name in (:names)")
    Set<TagDTO> findByNameIn(Set<String> names);

}