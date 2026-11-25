package com.kaucjuszGo.domain.ranking.dto;

import com.kaucjuszGo.domain.machines.dto.PageResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class RankingResponseDto {
    private PageResponseDto<RankingEntryDto> leaderboard;
    private RankingEntryDto currentUser;
}
