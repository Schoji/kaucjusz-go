package com.kaucjuszGo.domain.transaction;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    Page<TransactionEntity> findByUser_Id(UUID userId, Pageable pageable);

    boolean existsByReceiptScan_IdAndTransactionType(UUID receiptScanId, TransactionType type);
}
