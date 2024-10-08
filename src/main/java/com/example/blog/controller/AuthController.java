package com.example.blog.controller;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.jwt.JwtAuthRequest;
import com.example.blog.dto.jwt.JwtAuthResponse;
import com.example.blog.dto.user.UserFormDto;
import com.example.blog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request) {
        JwtAuthResponse jwtAuthResponse = this.authService.loginUser(request);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("register")
    ResponseEntity<ApiResponse> register(@Valid @RequestBody UserFormDto userFormDto) {
        return new ResponseEntity<>(this.authService.registerUser(userFormDto), HttpStatus.CREATED);
    }


}
