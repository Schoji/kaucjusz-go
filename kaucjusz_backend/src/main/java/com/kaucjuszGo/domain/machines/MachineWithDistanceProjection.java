package com.kaucjuszGo.domain.machines;

import java.util.UUID;

public interface MachineWithDistanceProjection {
    UUID getId();
    String getName();
    String getAddress();
    Double getLongitude();
    Double getLatitude();
    String getPhotoUrl();
    Double getAverageRating();
    Double getDistance();
}
