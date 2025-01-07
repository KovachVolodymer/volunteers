package org.example.volunteerback.dto;

public record FeeDTO(
        UserDTO user,
        String title,
        String image,
        Integer goal,
        Integer raised,
        String description,
        Integer count
)
{
}
