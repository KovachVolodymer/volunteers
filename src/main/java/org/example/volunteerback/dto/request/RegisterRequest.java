package org.example.volunteerback.dto.request;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
