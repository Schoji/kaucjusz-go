package com.kaucjuszGo.domain.machines.reviews;

import java.time.Instant;
import java.util.UUID;

public interface ReviewProjection {
    UUID getId();

    Integer getRating();

    String getComment();

    String getUserNickname();

    Instant getCreatedAt();

    Boolean getIsOwn();
}
