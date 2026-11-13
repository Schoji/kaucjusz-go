package com.kaucjuszGo.domain.machines;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaucjuszGo.domain.machines.dto.MachineWithDistanceProjection;

public interface MachineRepository extends JpaRepository<MachineEntity, UUID> {
        @Query("SELECT m.id AS id, m.name AS name, m.address AS address, m.photoUrl AS photoUrl, m.reviewsCount AS reviewsCount, "
                        +
                        "m.geolocation AS geolocation, m.averageRating AS averageRating, m.status AS status, "
                        +
                        "ST_Distance(m.geolocation, :position) AS distance " +
                        "FROM MachineEntity m " +
                        "WHERE (:queryPattern IS NULL OR LOWER(m.name) LIKE :queryPattern OR LOWER(m.address) LIKE :queryPattern) "
                        +
                        "AND (:isOpen IS NULL OR (m.status = 'ACTIVE' AND :isOpen = TRUE) OR (m.status != 'ACTIVE' AND :isOpen = FALSE)) "
                        +
                        "AND (:isTopRated IS NULL OR (m.averageRating >= 4.5 AND :isTopRated = TRUE) OR (m.averageRating < 4.5 AND :isTopRated = FALSE)) "
                        +
                        "AND (:radius IS NULL OR ST_Distance(m.geolocation, :position) <= :radius) " +
                        "ORDER BY ST_Distance(m.geolocation, :position) ASC")
        public List<MachineWithDistanceProjection> findMachinesWithFilters(@Param("position") Point position,
                        @Param("radius") Double radius, @Param("queryPattern") String queryPattern,
                        @Param("isOpen") Boolean isOpen,
                        @Param("isTopRated") Boolean isTopRated);

        @Query("SELECT m.externalId FROM MachineEntity m WHERE m.externalId IS NOT NULL")
        public Set<String> findAllExternalIds();
}
