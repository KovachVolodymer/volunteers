package org.example.volunteerback.dto;

import java.time.LocalDateTime;

public record DonationDTO(
        Long id,
        Long feeId,
        Long userId,
        Integer amount,
        LocalDateTime donatedAt
) {}
