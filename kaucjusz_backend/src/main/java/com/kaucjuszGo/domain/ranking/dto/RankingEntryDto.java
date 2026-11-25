package com.kaucjuszGo.domain.ranking.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class RankingEntryDto {
    private Long rank;
    private UUID userId;
    private String nickname;
    private String region;
    private Integer level;
    private Integer experience;
}
