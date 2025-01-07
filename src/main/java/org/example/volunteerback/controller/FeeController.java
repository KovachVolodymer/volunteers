package org.example.volunteerback.controller;

import org.example.volunteerback.model.fee.Fee;
import org.example.volunteerback.model.user.UserDetailsImpl;
import org.example.volunteerback.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/fees")
public class FeeController {

    private final FeeService feeService;

    @Autowired
    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getFeeById(@PathVariable Long id) {
        return feeService.getFeeById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Object> postFee(@RequestBody Fee fee, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return feeService.postFee(fee, userDetails.getId());
    }

    @GetMapping("/all")
    public List<Fee> getAllFee(){
      return feeService.getAllFee();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchFee (@PathVariable Long id,Fee updateFee){
        return feeService.patchFee(id,updateFee);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFee(@PathVariable Long id){
        return feeService.deleteFee(id);
    }

}
