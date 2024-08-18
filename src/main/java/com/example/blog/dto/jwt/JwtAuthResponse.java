package com.example.blog.dto.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse {
    private int status;
    private boolean success;
    private String token;
}