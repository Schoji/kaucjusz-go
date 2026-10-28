package com.kaucjuszGo.domain.review;

import java.time.Instant;
import java.util.UUID;

import com.kaucjuszGo.domain.machines.MachineEntity;
import com.kaucjuszGo.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "machine_reviews",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_machine_reviews_machine_user", columnNames = {"machine_id", "user_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
public class MachineReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    private MachineEntity machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
