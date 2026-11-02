package com.kaucjuszGo.domain.machines;

import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MachineRepository extends JpaRepository<MachineEntity, UUID> {
    @Query ("SELECT m.id AS id, m.name AS name, m.address AS address, m.photoUrl AS photoUrl, ST_X(m.geolocation) AS longitude, ST_Y(m.geolocation) AS latitude, m.averageRating AS averageRating, m.reviewsCount AS reviewsCount, m.status AS status, " +
           "ST_Distance(m.geolocation, :position) AS distance " +
           "FROM MachineEntity m " +
           "WHERE (:query IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.address) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:isOpen IS NULL OR (m.status = 'ACTIVE' AND :isOpen = TRUE) OR (m.status != 'ACTIVE' AND :isOpen = FALSE)) " +
           "AND (:isTopRated IS NULL OR (m.averageRating >= 4.5 AND :isTopRated = TRUE) OR (m.averageRating < 4.5 AND :isTopRated = FALSE)) " +
           "AND (:isNearby IS NULL OR (ST_Distance(m.geolocation, :position) <= :radius AND :isNearby = TRUE) OR (ST_Distance(m.geolocation, :position) > :radius AND :isNearby = FALSE))")
    public List<MachineWithDistanceProjection> findMachinesWithFilters(Point position, Double radius, String query, Boolean isOpen, Boolean isTopRated, Boolean isNearby);
}
