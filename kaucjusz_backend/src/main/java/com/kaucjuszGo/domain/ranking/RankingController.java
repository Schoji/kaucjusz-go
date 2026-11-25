package com.kaucjuszGo.domain.ranking;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaucjuszGo.domain.ranking.dto.RankingResponseDto;


@RestController
@RequestMapping("/api/rankings")
public class RankingController {
    private RankingService rankingService;
    
    RankingController(RankingService rankingService){
        this.rankingService = rankingService;
    }

    @GetMapping("/global")
    public ResponseEntity<RankingResponseDto> getGlobalRanking(@ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(rankingService.getPetRanking(pageable, false));
    }
    
    @GetMapping("/local")
    public ResponseEntity<RankingResponseDto> getLocalRanking(@ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(rankingService.getPetRanking(pageable, true));
    }
}
