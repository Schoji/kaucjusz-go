package com.kaucjuszGo.domain.machines.reviews;

import java.math.BigDecimal;

public interface MachineRatingStatsProjection {
    BigDecimal getAverageRating();

    Long getReviewsCount();
}
