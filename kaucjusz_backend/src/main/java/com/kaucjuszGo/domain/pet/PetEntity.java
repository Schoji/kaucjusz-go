package com.kaucjuszGo.domain.pet;

import java.time.Instant;
import java.util.UUID;

import com.kaucjuszGo.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "xp", nullable = false)
    private Integer experience = 0;

    @Column(name = "base_hunger", nullable = false)
    private Integer baseHunger = 100;

    @Column(name = "base_mood", nullable = false)
    private Integer baseMood = 100;

    @Column(name = "last_interaction_at", nullable = false)
    private Instant lastInteractionAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (lastInteractionAt == null) {
            lastInteractionAt = createdAt;
        }
        if (level == null) {
            level = 1;
        }
        if (experience == null) {
            experience = 0;
        }
        if (baseHunger == null) {
            baseHunger = 100;
        }
        if (baseMood == null) {
            baseMood = 100;
        }
    }

    public Integer getXp() {
        return experience;
    }

    public void setXp(Integer xp) {
        this.experience = xp;
    }

    public Instant getLastFed() {
        return lastInteractionAt;
    }

    public void setLastFed(Instant lastFed) {
        this.lastInteractionAt = lastFed;
    }
}
