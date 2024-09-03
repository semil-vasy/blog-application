package com.example.blog.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    @NotEmpty
    private String email;
    @NotEmpty
    private String username;
    @NotEmpty
    private String firstName;
    private String lastName;
    private String bio;
    private SocialLinksDTO socialLinks;
    private Date createdOn;
    private String profileImage;
}
