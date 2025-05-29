package org.example.volunteerback.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.volunteerback.model.user.ERole;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ERole name;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name.name();
    }

}
