package com.kaucjuszGo.domain.machines.reviews;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MachineReviewRepository extends JpaRepository<MachineReviewEntity, UUID> {
    @Query(value = """
            SELECT
                combined.id AS "id",
                combined.rating AS "rating",
                combined.comment AS "comment",
                combined."userNickname" AS "userNickname",
                combined."createdAt" AS "createdAt",
                combined."isOwn" AS "isOwn"
            FROM (
                (
                    SELECT
                        r.id,
                        r.rating,
                        r.comment,
                        u.nickname AS "userNickname",
                        r.created_at AS "createdAt",
                        TRUE AS "isOwn",
                        0 AS priority
                    FROM machine_reviews r
                    JOIN users u ON u.id = r.user_id
                    WHERE r.machine_id = :machineId AND r.user_id = :userId
                    ORDER BY r.created_at DESC, r.id DESC
                    LIMIT 5
                )
                UNION ALL
                (
                    SELECT
                        r.id,
                        r.rating,
                        r.comment,
                        u.nickname AS "userNickname",
                        r.created_at AS "createdAt",
                        FALSE AS "isOwn",
                        1 AS priority
                    FROM machine_reviews r
                    JOIN users u ON u.id = r.user_id
                    WHERE r.machine_id = :machineId AND r.user_id <> :userId
                )
            ) combined
            ORDER BY combined.priority ASC, combined."createdAt" DESC, combined.id DESC
            """, countQuery = """
            SELECT
                (SELECT COUNT(*) FROM (
                    SELECT 1
                    FROM machine_reviews r
                    WHERE r.machine_id = :machineId AND r.user_id = :userId
                    LIMIT 5
                ) own_reviews)
                +
                (SELECT COUNT(*)
                 FROM machine_reviews r
                 WHERE r.machine_id = :machineId AND r.user_id <> :userId)
            """, nativeQuery = true)
    Page<ReviewProjection> findPrioritizedReviewsByMachineId(
            @Param("machineId") UUID machineId,
            @Param("userId") UUID userId,
            Pageable pageable);

    @Query(value = """
            SELECT
                COALESCE(ROUND(AVG(latest.rating), 2), 0.00) AS "averageRating",
                COUNT(latest.rating) AS "reviewsCount"
            FROM (
                SELECT DISTINCT ON (r.user_id) r.rating
                FROM machine_reviews r
                WHERE r.machine_id = :machineId
                ORDER BY r.user_id, r.created_at DESC, r.id DESC
            ) latest
            """, nativeQuery = true)
    MachineRatingStatsProjection calculateRatingStatsByMachineId(@Param("machineId") UUID machineId);

    @Query("Select r.rating FROM MachineReviewEntity r WHERE r.machine.id = :machineId AND r.user.id = :userId ORDER BY r.createdAt DESC, r.id DESC LIMIT 1")
    public Integer findNewestMachineRatingById(@Param("machineId") UUID machineId, @Param("userId") UUID userId);
}
