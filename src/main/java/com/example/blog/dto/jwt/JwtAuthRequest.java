package com.example.blog.dto.jwt;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String username;
    private String password;
}
