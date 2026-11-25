package com.kaucjuszGo.domain.transaction;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaucjuszGo.domain.machines.dto.PageResponseDto;
import com.kaucjuszGo.domain.transaction.dto.PointTransactionDto;
import com.kaucjuszGo.domain.transaction.dto.PointsBalanceDto;

@RequestMapping("/api/points")
@RestController
public class PointsController {
    private final PointService pointService;

    PointsController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/balance")
    public ResponseEntity<PointsBalanceDto> getUserBalance() {
        return ResponseEntity.ok(pointService.getCurrentUserBalance());
    }

    @GetMapping("/transactions")
    public ResponseEntity<PageResponseDto<PointTransactionDto>> getUserTransactions(
            @ParameterObject @PageableDefault(page = 0, size = 20) Pageable pageable) {
        return ResponseEntity.ok(pointService.getCurrentUserTransactions(pageable));
    }

}
