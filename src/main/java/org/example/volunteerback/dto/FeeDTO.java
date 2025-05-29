package org.example.volunteerback.dto;

import org.example.volunteerback.dto.user.UserDTO;

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
