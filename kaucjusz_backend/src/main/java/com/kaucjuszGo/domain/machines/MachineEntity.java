package com.kaucjuszGo.domain.machines;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import com.kaucjuszGo.domain.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vending_machines")
@Getter
@Setter
@NoArgsConstructor
public class MachineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "external_id", unique = true, length = 100)
    private String externalId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column (name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "postal_code", length = 6)
    private String postalCode;

    @Column(name = "location", nullable = false, columnDefinition = "geography(Point, 4326)")
    private Point geolocation;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "opening_hours", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private OpeningHours openingHours;

    @Column(name = "average_rating", nullable = false, precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.valueOf(0.00);

    @Column(name = "reviews_count", nullable = false)
    private Integer reviewsCount = 0;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private MachineStatus status = MachineStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_user_id")
    private UserEntity reportedByUser;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (averageRating == null) {
            averageRating = BigDecimal.valueOf(0.00);
        }
        if (reviewsCount == null) {
            reviewsCount = 0;
        }
        if (status == null) {
            status = MachineStatus.ACTIVE;
        }
    }

    public Integer getReviewCount() {
        return reviewsCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewsCount = reviewCount;
    }

    public UUID getReportedByUserId() {
        return reportedByUser != null ? reportedByUser.getId() : null;
    }
}
