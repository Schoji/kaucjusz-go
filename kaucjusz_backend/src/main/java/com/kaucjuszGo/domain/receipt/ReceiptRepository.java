package com.kaucjuszGo.domain.receipt;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, UUID> {

}
