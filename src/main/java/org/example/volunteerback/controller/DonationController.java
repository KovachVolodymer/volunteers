package org.example.volunteerback.controller;

import org.example.volunteerback.dto.DonationDTO;
import org.example.volunteerback.dto.DonationRequestDTO;
import org.example.volunteerback.model.user.UserDetailsImpl;
import org.example.volunteerback.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees/{feeId}/donations")
public class DonationController {

    private final DonationService donationService;

    @Autowired
    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<Object> addDonation(@PathVariable Long feeId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestBody DonationRequestDTO donationRequestDTO) {
        return donationService.addDonation(feeId, userDetails.getId(), donationRequestDTO);
    }

    @GetMapping
    public List<DonationDTO> getAllDonationsForFee(@PathVariable Long feeId){
        List<DonationDTO> donations = donationService.getAllDonationsForFee(feeId);
        return ResponseEntity.ok(donations).getBody();
    }

    @GetMapping("/{donationId}")
    public DonationDTO getDonation(@PathVariable Long donationId, @PathVariable String feeId){
        DonationDTO donation = donationService.getDonationById(donationId);
        return ResponseEntity.ok(donation).getBody();
    }

}
