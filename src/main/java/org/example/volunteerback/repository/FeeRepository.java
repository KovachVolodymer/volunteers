package org.example.volunteerback.repository;

import org.example.volunteerback.model.fee.Fee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<Fee> findById(Long id);

    @EntityGraph(attributePaths = {"user"})
    List<Fee> findAll();

}