package com.example.blog.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialLinksDTO implements Serializable {
    private String youtube;
    private String instagram;
    private String facebook;
    private String twitter;
    private String github;
    private String website;
}
