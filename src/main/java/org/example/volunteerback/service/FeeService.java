package org.example.volunteerback.service;

import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.model.fee.Fee;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.FeeRepository;
import org.example.volunteerback.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class FeeService {

    private final FeeRepository feeRepository;
    private final UserRepository userRepository;
    public FeeService(FeeRepository feeRepository, UserRepository userRepository) {
        this.feeRepository = feeRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> postFee(Fee fee,Long id) {
        Optional<User> user = userRepository.findById(id);
        fee.setUser(user.get());
        feeRepository.save(fee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body( new MessageResponse("Fee created"));
    }

    public Fee getFeeById(Long id) {
        return feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee not found with id: " + id));
    }

}
