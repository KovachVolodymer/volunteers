package org.example.volunteerback.mapper;

import org.example.volunteerback.dto.donation.DonationDTO;
import org.example.volunteerback.model.donation.Donation;
import org.springframework.stereotype.Component;

@Component
public class DonationMapper {

    public DonationDTO toDTO(Donation donation) {
        return new DonationDTO(
                donation.getId(),
                donation.getFee().getId(),
                donation.getUser().getId(),
                donation.getAmount(),
                donation.getDonatedAt()
        );
    }

}
