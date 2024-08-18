package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String blogId;

    @Column(nullable = false)
    private String title;

    @Column
    private String banner;

    @Column(length = 200)
    private String description;

    @Column
    private String content;

    @ElementCollection
    private List<String> tags;

    @Column(nullable = false)
    private int totalLikes = 0;

    @Column(nullable = false)
    private int totalComments = 0;

    @Column(nullable = false)
    private int totalReads = 0;

    @Column(name = "total_parent_comments", nullable = false)
    private int totalParentComments = 0;

    @Column(nullable = false)
    private boolean draft = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(name = "published_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp publishedAt;

}
