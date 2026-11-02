package com.kaucjuszGo.domain.machines.reviews;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineReviewRepository extends JpaRepository<MachineReviewEntity, UUID> {
    @EntityGraph(attributePaths = {"user"})
    public Page<MachineReviewEntity> findByMachine_Id(UUID machineId, Pageable pageable);
}
