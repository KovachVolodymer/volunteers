package org.example.volunteerback.dto.request;

public record RegisterRequest(
        String fullName,
        String email,
        String password
) {
}
