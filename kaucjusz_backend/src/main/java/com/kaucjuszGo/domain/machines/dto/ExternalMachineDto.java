package com.kaucjuszGo.domain.machines.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ExternalMachineDto {
    private String brand;
    private String city;
    private String address;
    @JsonProperty("postcode")
    private String postalCode;
    @JsonProperty ("lat")
    private Double latitude;
    @JsonProperty ("lng")
    private Double longitude;
}
