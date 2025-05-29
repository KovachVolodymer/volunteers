package org.example.volunteerback.dto.user;

import jakarta.validation.constraints.*;

public record UserAuthDTO(
        @NotBlank(message = "First name cannot be empty")
        String firstName,

        @NotBlank(message = "Last name cannot be empty")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is invalid")
        @Size(max = 50, message = "Email must be less than 50 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Password must contain at least one uppercase letter," +
                        " one lowercase letter and one number")
        @Size(min = 6, message = "The password must contain at least 6 characters")
        String password,
        String photo,
        String description

) {
        public UserAuthDTO(String firstName, String lastName, String email, String password) {
                this(firstName, lastName, email, password, "", "");
        }

        public UserAuthDTO(String email, String password) {
                this("", "", email, password, "", "");
        }
}
