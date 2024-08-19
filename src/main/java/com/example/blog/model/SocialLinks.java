package com.example.blog.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class SocialLinks implements Serializable {
    private String youtube = "";
    private String instagram = "";
    private String facebook = "";
    private String twitter = "";
    private String github = "";
    private String website = "";
}
