package com.kaucjuszGo.domain.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaucjuszGo.domain.pet.PetEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    public Optional<UserEntity> findUserByFirebaseUid(String firebaseId);

    public boolean existsByNickname(String nickname);

    public Optional<PetEntity> findPetByUserFirebaseUid(UUID id);
}
