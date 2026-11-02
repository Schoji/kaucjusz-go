package com.kaucjuszGo.domain.machines.reviews;


import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ReviewDto {
    private UUID id;
    private Integer rating;
    private String comment;
    private String user;
    private Instant createdAt;
}
