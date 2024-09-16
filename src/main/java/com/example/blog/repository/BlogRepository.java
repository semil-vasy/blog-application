package com.example.blog.repository;

import com.example.blog.model.Blog;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByUser(User user);


    @Query("select p from Blog p where lower(p.title) LIKE :key")
    List<Blog> findByBlogTitleLike(@Param("key") String keyword);

    @Query(nativeQuery = true,value = "select sum(total_reads), count(1) from blog where user_id = :userId\n")
    Map<String,String> getBlogCountAndBlogReads(long userId);
}
