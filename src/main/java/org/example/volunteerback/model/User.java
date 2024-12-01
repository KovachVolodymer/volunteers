package org.example.volunteerback.model;

import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private String photo;

    public User(){};

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
