package com.example.blog.repository;

import com.example.blog.dto.blog.TagDTO;
import com.example.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select new com.example.blog.dto.blog.TagDTO(t.tagId, t.tagName) from Tag t where t.tagName in (:names)")
    Set<TagDTO> findByNameIn(Set<String> names);

    @Query(value = "select * from tags order by tag_id desc limit 10", nativeQuery = true)
    List<Tag> latestTags();
}