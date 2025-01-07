package org.example.volunteerback.service;

import org.example.volunteerback.dto.FeeDTO;
import org.example.volunteerback.dto.response.MessageResponse;
import org.example.volunteerback.mapper.FeeMapper;
import org.example.volunteerback.model.fee.Fee;
import org.example.volunteerback.model.user.User;
import org.example.volunteerback.repository.FeeRepository;
import org.example.volunteerback.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FeeService {

    private final FeeRepository feeRepository;
    private final UserRepository userRepository;
    private final FeeMapper feeMapper;

    public FeeService(FeeRepository feeRepository, UserRepository userRepository, FeeMapper feeMapper) {
        this.feeRepository = feeRepository;
        this.userRepository = userRepository;
        this.feeMapper = feeMapper;
    }

    public ResponseEntity<Object> postFee(Fee fee, Long id) {
        Optional<User> user = userRepository.findById(id);
        fee.setUser(user.get());
        feeRepository.save(fee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Fee created"));
    }

    public ResponseEntity<Object> getFeeById(Long id) {
        Fee fee= feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee not found with id: " + id));
        FeeDTO feeDTO = feeMapper.feeDTO(fee);
        return ResponseEntity.status(HttpStatus.OK).body(feeDTO);
    }

    public List<FeeDTO> getAllFee() {
        List<Fee> fees = feeRepository.findAll();
        return fees.stream()
                .map(feeMapper::feeDTO)
                .toList();
    }

    public ResponseEntity<Object> deleteFee(Long id) {
        if (!feeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Fee with id " + id + " not found"));
        }

        feeRepository.deleteById(id);
        return ResponseEntity.ok().body(new MessageResponse("delete success"));
    }

    public ResponseEntity<Object> patchFee(Long id, Fee newFee) {
        Optional<Fee> optionalFee = feeRepository.findById(id);
        if (optionalFee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fee is not found");
        }

        Fee fee = optionalFee.get();
        Optional.ofNullable(newFee.getCount()).ifPresent(fee::setCount);
        Optional.ofNullable(newFee.getGoal()).ifPresent(fee::setGoal);
        Optional.ofNullable(newFee.getDescription()).ifPresent(fee::setDescription);
        Optional.ofNullable(newFee.getImage()).ifPresent(fee::setImage);
        Optional.ofNullable(newFee.getRaised()).ifPresent(fee::setRaised);
        Optional.ofNullable(newFee.getTitle()).ifPresent(fee::setTitle);
        if (newFee.getUser() != null){
            User user = userRepository.findById(newFee.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
            fee.setUser(user);
        }
        Optional.ofNullable(newFee.getUser()).ifPresent(fee::setUser);

        feeRepository.save(fee);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Update success"));
    }

}
