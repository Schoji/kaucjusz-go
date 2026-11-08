package com.kaucjuszGo.domain.machines.reviews;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MachineReviewRepository extends JpaRepository<MachineReviewEntity, UUID> {
    @EntityGraph(attributePaths = {"user"})
    public Page<MachineReviewEntity> findByMachine_Id(UUID machineId, Pageable pageable);

    public Optional<MachineReviewEntity> findByUser_FirebaseUidAndMachine_Id(String firebaseUid, UUID machineId);

    @Query("SELECT AVG(r.rating) FROM MachineReviewEntity r WHERE r.machine.id = :machineId")
    public Double calculateAverageRatingByMachine_Id(UUID machineId);
}
