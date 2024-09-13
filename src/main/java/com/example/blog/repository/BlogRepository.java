package com.example.blog.repository;

import com.example.blog.dto.blog.BlogCustomDTO;
import com.example.blog.model.Blog;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByUser(User user);


    @Query("select p from Blog p where lower(p.title) LIKE :key")
    List<Blog> findByBlogTitleLike(@Param("key") String keyword);

    @Query(value = """
            SELECT b.id as id,
                   b.blog_id as blogId,
                   b.content as content,
                   b.title as title,
                   b.description as description,
                   b.banner as banner,
                   b.created_on as createdOn,
                   u.username as username,
                   u.profile_image as profileImage,
                   b.total_likes as totalLikes,
                   b.total_reads as totalReads,
                   t.tag_names as tagNames
            FROM blog b
                     INNER JOIN users u ON u.user_id = b.user_id
                     LEFT JOIN (SELECT bt.blog_id, STRING_AGG(t.tag_name, ', ') as tag_names
                                FROM blog_tags bt
                                         JOIN tags t ON t.tag_id = bt.tag_id
                                WHERE :tagName = ''
                                   OR t.tag_name = :tagName
                                GROUP BY bt.blog_id) t ON t.blog_id = b.id
            WHERE b.draft IS FALSE
              AND b.is_deleted = 0
              AND (:userId = 0 OR u.user_id = :userId)
            ORDER BY b.created_on DESC
            OFFSET :offset LIMIT :limit""", nativeQuery = true)
    List<BlogCustomDTO> getAllBlogs(@Param("userId") long userId, @Param("offset") int offset, @Param("limit") int limit, @Param("tagName") String tagName);

    @Query(value = """    
            SELECT b.id as id,
                   b.blog_id as blogId,
                   b.content as content,
                   b.title as title,
                   b.description as description,
                   b.banner as banner,
                   b.created_on as createdOn,
                   u.username as username,
                   u.profile_image as profileImage,
                   b.total_likes as totalLikes,
                   b.total_reads as totalReads,
                   t.tag_names as tagNames,
                   (b.total_comments * 3 + b.total_likes * 2 + b.total_reads) *
                   (1.0 / (EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - b.created_on)) / 86400 + 1)) AS trendingScore
            FROM blog b
                     INNER JOIN users u ON u.user_id = b.user_id
                     LEFT JOIN (SELECT bt.blog_id, STRING_AGG(t.tag_name, ', ') as tag_names
                                FROM blog_tags bt
                                         JOIN tags t ON t.tag_id = bt.tag_id
                                GROUP BY bt.blog_id) t ON t.blog_id = b.id
            WHERE b.draft = FALSE
              AND b.is_deleted = 0
            ORDER BY trendingScore DESC
            LIMIT 5""", nativeQuery = true)
    List<BlogCustomDTO> getTrendingBlogs();

    @Query(value = """
            SELECT b.id as id,
                   b.blog_id as blogId,
                   b.content as content,
                   b.title as title,
                   b.description as description,
                   b.banner as banner,
                   b.created_on as createdOn,
                   u.username as username,
                   u.profile_image as profileImage,
                   b.total_likes as totalLikes,
                   b.total_reads as totalReads,
                   t.tag_names as tagNames
            FROM blog b
                     INNER JOIN users u ON u.user_id = b.user_id
                     LEFT JOIN (SELECT bt.blog_id, STRING_AGG(t.tag_name, ', ') as tag_names
                                FROM blog_tags bt
                                         JOIN tags t ON t.tag_id = bt.tag_id
                                GROUP BY bt.blog_id) t ON t.blog_id = b.id
            WHERE b.draft IS FALSE
              AND b.is_deleted = 0
              AND b.blog_id = :blogId""", nativeQuery = true)
    BlogCustomDTO getBlogByBlogId(@Param("blogId") String blogId);


}
