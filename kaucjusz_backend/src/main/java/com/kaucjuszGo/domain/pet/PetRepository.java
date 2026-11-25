package com.kaucjuszGo.domain.pet;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PetRepository extends JpaRepository<PetEntity, UUID> {
    @EntityGraph(attributePaths = {"user"})
    public Page<PetEntity> findByUser_DeletedAtIsNull(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    public Page<PetEntity> findByUser_DeletedAtIsNullAndUser_RegionIgnoreCase(String region, Pageable pageable);

    @Query("SELECT COUNT(p) FROM PetEntity AS p " + 
        "WHERE p.user.deletedAt IS NULL " +
        "AND (p.level > :level " +
        "OR (p.level = :level AND p.experience > :xp) "+
        "OR (p.level = :level AND p.experience = :xp AND p.createdAt < :createdAt))")
    public Long countPlayersAhead(@Param("level") Integer level, @Param("xp") Integer xp, @Param("createdAt") Instant createdAt);

    @Query("SELECT COUNT(p) FROM PetEntity AS p " + 
        "WHERE p.user.deletedAt IS NULL "+
        "AND LOWER(p.user.region) = LOWER(:region) " +
        "AND (p.level > :level "+
        "OR (p.level = :level AND p.experience > :xp) "+
        "OR (p.level = :level AND p.experience = :xp AND p.createdAt < :createdAt))")
    public Long countPlayersAheadInRegion(@Param("region") String region, @Param("level") Integer level, @Param("xp") Integer xp, @Param("createdAt") Instant createdAt);
}
