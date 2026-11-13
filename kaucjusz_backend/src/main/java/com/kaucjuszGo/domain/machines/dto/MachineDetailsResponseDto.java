package com.kaucjuszGo.domain.machines.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.kaucjuszGo.domain.machines.MachineStatus;
import com.kaucjuszGo.domain.machines.OpeningHours;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class MachineDetailsResponseDto {
    private UUID id;
    private String name;
    private String address;
    private String photoUrl;
    private OpeningHours openingHours;
    private BigDecimal averageRating;
    private Integer reviewsCount;
    private MachineStatus status;
}
