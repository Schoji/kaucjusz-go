package com.kaucjuszGo.domain.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    public Optional<UserEntity> findUserByFirebaseUid(String firebaseId);

    public boolean existsByNickname(String nickname);
}
