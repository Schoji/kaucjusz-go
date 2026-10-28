package com.kaucjuszGo.domain.receipt;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.kaucjuszGo.domain.machines.MachineEntity;
import com.kaucjuszGo.domain.user.UserEntity;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "receipt_scans")
@Getter
@Setter
@NoArgsConstructor
public class ReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_machine_id")
    private MachineEntity machine;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ScanType scanType;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PROCESSED";

    @Column(name = "receipt_fingerprint", unique = true, length = 255)
    private String receiptFingerprint;

    @Column(name = "store_name", length = 100)
    private String storeName;

    @Column(name = "deposit_amount", precision = 6, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "bottles_count", nullable = false)
    private Integer bottlesCount = 0;

    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded = 0;

    @Column(name = "printed_at")
    private Instant printedAt;

    @Column(name = "scan_location", columnDefinition = "geography(Point, 4326)")
    private Point scanLocation;

    @Column(name = "scanned_at", nullable = false, updatable = false)
    private Instant scannedAt;

    @PrePersist
    protected void prePersist() {
        if (scannedAt == null) {
            scannedAt = Instant.now();
        }
        if (bottlesCount == null) {
            bottlesCount = 0;
        }
        if (pointsAwarded == null) {
            pointsAwarded = 0;
        }
        if (status == null) {
            status = "PROCESSED";
        }
    }

    public Point getLocation() {
        return scanLocation;
    }

    public void setLocation(Point location) {
        this.scanLocation = location;
    }
}