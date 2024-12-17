package org.example.volunteerback.dto.response;

public record UserResponseDTO (
        String firsName,
        String lastName,
        String email,
        String password,
        String photo,
        String description
)
{}
