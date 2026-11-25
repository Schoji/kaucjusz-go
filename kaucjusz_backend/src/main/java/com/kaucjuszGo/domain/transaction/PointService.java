package com.kaucjuszGo.domain.transaction;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaucjuszGo.core.exception.DuplicatePointsAwardException;
import com.kaucjuszGo.core.exception.InvalidPointOperationException;
import com.kaucjuszGo.domain.machines.dto.PageResponseDto;
import com.kaucjuszGo.domain.receipt.ReceiptEntity;
import com.kaucjuszGo.domain.transaction.dto.PointTransactionDto;
import com.kaucjuszGo.domain.transaction.dto.PointsBalanceDto;
import com.kaucjuszGo.domain.user.UserEntity;
import com.kaucjuszGo.domain.user.UserService;

@Service
public class PointService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    PointService(UserService userService, TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public void awardPoints(UUID userId, int amount, TransactionType type, ReceiptEntity receiptScan) {
        if (amount <= 0 || type != TransactionType.SCAN || receiptScan == null) {
            throw new InvalidPointOperationException("Invalid point service - award points");
        }

        UserEntity user = userService.getActiveUserForUpdate(userId);

        if (type == TransactionType.SCAN && transactionRepository
                .existsByReceiptScan_IdAndTransactionType(receiptScan.getId(), TransactionType.SCAN)) {
            throw new DuplicatePointsAwardException("Duplicate reciept request - reciept already in database");
        }

        try {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setPointsAmount(amount);
            transaction.setReceiptScan(receiptScan);
            transaction.setTransactionType(type);
            transaction.setUser(user);

            user.creditPoints(amount);

            transactionRepository.saveAndFlush(transaction);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatePointsAwardException("Points already awarded for this receipt");
        }
    }

    @Transactional
    public void spendPoints(UUID userId, int amount, TransactionType type) {
        if (amount <= 0 || (type != TransactionType.FEED_PET && type != TransactionType.BUY_ITEM)) {
            throw new InvalidPointOperationException("Invalid point service - spend points");
        }

        UserEntity user = userService.getActiveUserForUpdate(userId);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setPointsAmount(-amount);
        transaction.setTransactionType(type);
        transaction.setUser(user);

        user.debitPoints(amount);

        transactionRepository.saveAndFlush(transaction);
    }

    @Transactional(readOnly = true)
    public PointsBalanceDto getCurrentUserBalance() {
        UserEntity user = userService.getCurrentUser();

        return PointsBalanceDto.from(user);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<PointTransactionDto> getCurrentUserTransactions(Pageable pageable) {
        Sort transactionSort = Sort.by(
                Sort.Order.desc(TransactionEntity::getCreatedAt),
                Sort.Order.desc(TransactionEntity::getId));
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), transactionSort);

        UserEntity user = userService.getCurrentUser();

        Page<TransactionEntity> transaction = transactionRepository.findByUser_Id(user.getId(), sortedPageable);

        Page<PointTransactionDto> mapped = transaction.map(PointTransactionDto::from);
        return PageResponseDto.from(mapped);
    }
}
