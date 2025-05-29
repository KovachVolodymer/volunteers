package org.example.volunteerback.dto.donation;

import java.time.LocalDateTime;

public record DonationDTO(
        Long id,
        Long feeId,
        Long userId,
        Integer amount,
        LocalDateTime donatedAt
) {}
