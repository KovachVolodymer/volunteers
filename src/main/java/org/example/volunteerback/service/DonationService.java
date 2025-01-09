package org.example.volunteerback.service;

import org.example.volunteerback.dto.DonationDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.model.donation.Donation;
import org.example.volunteerback.model.fee.Fee;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.DonationRepository;
import org.example.volunteerback.repository.FeeRepository;
import org.example.volunteerback.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final FeeRepository feeRepository;

    public DonationService(DonationRepository donationRepository, UserRepository userRepository, FeeRepository feeRepository) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.feeRepository = feeRepository;
    }

    public ResponseEntity<Object> addDonation(Long feeId, Long userId, DonationDTO donation){
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new IllegalArgumentException("Fee is not found"));
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        fee.setRaised((fee.getRaised() == null ? 0 : fee.getRaised()) + donation.amount());
        fee.setCount((fee.getCount() == null ? 0 : fee.getCount()) + 1);
        if(donation.amount()<0)
        {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("donation is incorrect");
        }
        fee.setRaised(fee.getRaised()+donation.amount());
        fee.setCount(fee.getCount()+1);


        Donation donationSave = new Donation(fee, user, donation.amount());
        donationRepository.save(donationSave);
        return ResponseEntity.ok().body(new MessageResponse("DDonation successful!"));
    }

}
