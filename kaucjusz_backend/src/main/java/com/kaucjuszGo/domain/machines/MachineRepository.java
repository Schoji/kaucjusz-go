package com.kaucjuszGo.domain.machines;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<MachineEntity, UUID> {

}
