package com.example.blog.service;

import com.example.blog.dto.user.UserDTO;
import com.example.blog.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDTO updateUser(UserDTO userDTO);

    UserDTO getUserByUsername(String username);

    List<User> getAllUsers();

    void deleteUser(Long userId);

    User getAuthUser();
    List<UserDTO> getUserBySearchKey(String searchKey);

    String uploadImage(long userId, MultipartFile image);

}
