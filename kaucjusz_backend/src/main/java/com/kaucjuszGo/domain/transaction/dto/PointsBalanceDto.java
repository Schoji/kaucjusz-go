package com.kaucjuszGo.domain.transaction.dto;

import com.kaucjuszGo.domain.user.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointsBalanceDto {
    private Integer currentPoints;
    private Integer lifetimePoints;

    public static PointsBalanceDto from(UserEntity user) {
        PointsBalanceDto pointsBalanceDto = new PointsBalanceDto();
        pointsBalanceDto.setCurrentPoints(user.getCurrentPoints());
        pointsBalanceDto.setLifetimePoints(user.getLifetimePoints());
        return pointsBalanceDto;
    }
}
