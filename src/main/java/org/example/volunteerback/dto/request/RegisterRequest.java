package org.example.volunteerback.dto.request;

public record RegisterRequest(
        String email,
        String password
) {
}
