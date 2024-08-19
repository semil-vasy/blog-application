package com.example.blog.dto.jwt;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String email;
    private String password;
}
