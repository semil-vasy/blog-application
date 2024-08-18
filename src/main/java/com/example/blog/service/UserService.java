package com.example.blog.service;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.jwt.JwtAuthRequest;
import com.example.blog.dto.jwt.JwtAuthResponse;
import com.example.blog.dto.user.UserFormDto;

import java.util.List;

public interface UserService {

    ApiResponse registerUser(UserFormDto userFormDto);

    JwtAuthResponse loginUser(JwtAuthRequest request);

    UserFormDto updateUser(UserFormDto userFormDto, Long userId);

    UserFormDto getUserById(Long userId);

    List<UserFormDto> getAllUsers();

    void deleteUser(Long userId);

}
