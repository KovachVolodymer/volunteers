package org.example.volunteerback.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO (
        String firstName,

        String lastName,


        @Email(message = "Email is invalid")
        @Size(max = 50, message = "Email must be less than 50 characters")
        String email,


        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Password must contain at least one uppercase letter," +
                        " one lowercase letter and one number")
        @Size(min = 6, message = "The password must contain at least 6 characters")
        String password,
        String photo,
        String description
)
{}
