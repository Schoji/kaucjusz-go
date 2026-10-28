package com.kaucjuszGo.domain.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "firebase_uid", nullable = false, unique = true, length = 128)
    private String firebaseUid;

    @Column(name = "nickname", unique = true, length = 50)
    private String nickname;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "current_points", nullable = false)
    private Integer currentPoints = 0;

    @Column(name = "lifetime_points", nullable = false)
    private Integer lifetimePoints = 0;

    @Column(name = "current_streak", nullable = false)
    private Integer currentStreak = 0;

    @Column(name = "last_scan_date")
    private LocalDate lastScanDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (currentPoints == null) {
            currentPoints = 0;
        }
        if (lifetimePoints == null) {
            lifetimePoints = 0;
        }
        if (currentStreak == null) {
            currentStreak = 0;
        }
    }

    public String getFirebaseId() {
        return firebaseUid;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseUid = firebaseId;
    }

    public Integer getTotalPoints() {
        return lifetimePoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.lifetimePoints = totalPoints;
    }
}
