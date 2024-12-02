package org.example.volunteerback.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
