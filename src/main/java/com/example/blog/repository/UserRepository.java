package com.example.blog.repository;

import com.example.blog.dto.user.UserDTO;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users u ORDER BY u.user_id ASC", nativeQuery = true)
    List<User> findAll();

    @Query(value = "select * from users u WHERE u.email = :email and u.is_deleted = 0", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query("SELECT new com.example.blog.dto.user.UserDTO(u.email, u.username, u.firstName, u.lastName, u.bio, " +
            "new com.example.blog.dto.user.SocialLinksDTO(u.socialLinks.youtube, u.socialLinks.instagram, u.socialLinks.facebook, u.socialLinks.twitter, u.socialLinks.github, u.socialLinks.website), " +
            "u.createdOn, u.profileImage) " +
            "FROM User u WHERE u.username = :username and u.isDeleted = 0")
    UserDTO getDTOByUsername(String username);

}
