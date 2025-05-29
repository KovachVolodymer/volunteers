package org.example.volunteerback.repository;

import org.example.volunteerback.model.Role;
import org.example.volunteerback.model.user.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
