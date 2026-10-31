package com.kaucjuszGo.domain.pet;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PetRepository extends JpaRepository<PetEntity, UUID> {

}
