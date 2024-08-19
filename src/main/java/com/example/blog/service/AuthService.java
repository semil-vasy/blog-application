package com.example.blog.service;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.jwt.JwtAuthRequest;
import com.example.blog.dto.jwt.JwtAuthResponse;
import com.example.blog.dto.user.UserFormDto;

public interface AuthService {
    ApiResponse registerUser(UserFormDto userFormDto);

    JwtAuthResponse loginUser(JwtAuthRequest request);
}
