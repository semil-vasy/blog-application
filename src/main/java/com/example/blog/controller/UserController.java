package com.example.blog.controller;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.user.UserDTO;
import com.example.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(this.userService.getUserByUsername(username));
    }

    @GetMapping("search")
    ResponseEntity<List<UserDTO>> searchUsers(@RequestParam(defaultValue = "") String searchKey){
        return ResponseEntity.ok(this.userService.getUserBySearchKey(searchKey));
    }

    @PutMapping
    ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = this.userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("{id}")
    ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") long userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse(200, true, "User Deleted Successfully"), HttpStatus.OK);
    }

}
