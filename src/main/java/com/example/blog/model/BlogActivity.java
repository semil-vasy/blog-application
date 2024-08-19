package com.example.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class BlogActivity {

    @Column(columnDefinition = "int default 0")
    private int totalLikes = 0;

    @Column(columnDefinition = "int default 0")
    private int totalComments = 0;

    @Column(columnDefinition = "int default 0")
    private int totalReads = 0;
}
