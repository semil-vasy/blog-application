package com.example.blog.controller;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.user.UserFormDto;
import com.example.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    ResponseEntity<List<UserFormDto>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("{id}")
    ResponseEntity<UserFormDto> getUserById(@PathVariable("id") long userId) {
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }

    @PutMapping("{id}")
    ResponseEntity<UserFormDto> updateUser(@Valid @PathVariable("id") long userId, @RequestBody UserFormDto userFormDto) {
        UserFormDto updatedUser = this.userService.updateUser(userFormDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") long userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse(200, true, "User Deleted Successfully"), HttpStatus.OK);
    }

}
