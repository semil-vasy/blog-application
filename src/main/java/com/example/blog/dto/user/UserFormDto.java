package com.example.blog.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserFormDto {

    private long id;

    @NotEmpty
    @Size(min = 3, message = "Username must be min of 3 characters !!")
    private String firstName;

    @Email(message = "Email address is not valid !!")
    private String email;

    @NotEmpty
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$%^&+=) !!"
    )
    private String password;

    @NotEmpty
    private String bio;

}
