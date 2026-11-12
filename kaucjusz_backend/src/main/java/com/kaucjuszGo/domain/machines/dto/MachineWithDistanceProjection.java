package com.kaucjuszGo.domain.machines.dto;

import java.util.UUID;

import org.locationtech.jts.geom.Point;

import com.kaucjuszGo.domain.machines.MachineStatus;

public interface MachineWithDistanceProjection {
    UUID getId();

    String getName();

    String getAddress();

    Point getGeolocation();

    String getPhotoUrl();

    Double getAverageRating();

    Double getDistance();

    Integer getReviewsCount();

    MachineStatus getStatus();

    default Double getLongitude() {
        return getGeolocation() != null ? getGeolocation().getX() : null;
    }

    default Double getLatitude() {
        return getGeolocation() != null ? getGeolocation().getY() : null;
    }
}
