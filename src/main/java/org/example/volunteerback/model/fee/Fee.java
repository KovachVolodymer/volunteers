package org.example.volunteerback.model.fee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.volunteerback.model.user.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "fees")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String image;
    private Integer goal;
    private Integer raised;

    private String description;

}
