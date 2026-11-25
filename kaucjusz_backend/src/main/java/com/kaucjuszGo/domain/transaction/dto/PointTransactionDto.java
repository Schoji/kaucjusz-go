package com.kaucjuszGo.domain.transaction.dto;

import java.time.Instant;
import java.util.UUID;

import com.kaucjuszGo.domain.transaction.TransactionEntity;
import com.kaucjuszGo.domain.transaction.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransactionDto {
    private UUID id;
    private Integer pointsAmount;
    private TransactionType transactionType;
    private UUID receiptScanId;
    private Instant createdAt;

    public static PointTransactionDto from(TransactionEntity entity) {
        PointTransactionDto pointTransactionDto = new PointTransactionDto();
        pointTransactionDto.setId(entity.getId());
        pointTransactionDto.setPointsAmount(entity.getPointsAmount());
        pointTransactionDto
                .setReceiptScanId(entity.getReceiptScan() != null ? entity.getReceiptScan().getId() : null);
        pointTransactionDto.setTransactionType(entity.getTransactionType());
        pointTransactionDto.setCreatedAt(entity.getCreatedAt());

        return pointTransactionDto;
    }
}
