package com.kaucjuszGo.domain.machines.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class MachineListResponseDto {
    private UUID id;
    private Double longitude;
    private Double latitude;
    private String name;
    private String address;
    private Double distance;
    private Double averageRating;
    private String photoUrl;
}
