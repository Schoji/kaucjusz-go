package com.kaucjuszGo.domain.machines;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.kaucjuszGo.domain.machines.reviews.ReviewDto;

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
    private String status;
    private Page<ReviewDto> reviews;
}
