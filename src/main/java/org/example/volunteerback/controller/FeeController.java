package org.example.volunteerback.controller;

import org.example.volunteerback.model.fee.Fee;
import org.example.volunteerback.model.user.UserDetailsImpl;
import org.example.volunteerback.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/fees")
public class FeeController {

    private final FeeService feeService;

    @Autowired
    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fee> getFeeById(@PathVariable Long id) {
        return ResponseEntity.ok(feeService.getFeeById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Object> postFee(@RequestBody Fee fee,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  feeService.postFee(fee, userDetails.getId());
    }
}
