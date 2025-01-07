package org.example.volunteerback.model.donation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.volunteerback.model.fee.Fee;
import org.example.volunteerback.model.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer amount;

    private LocalDateTime donatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id", nullable = false)
    private Fee fee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.donatedAt = LocalDateTime.now(); // Автоматичне встановлення дати при створенні
    }
}
