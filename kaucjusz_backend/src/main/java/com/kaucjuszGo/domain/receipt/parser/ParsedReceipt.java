package com.kaucjuszGo.domain.receipt.parser;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;

public record ParsedReceipt(
        RetailOperator operator,
        ReceiptScanStatus status,
        String rawBarcode,
        Integer depositAmountGrosze,
        Integer nonDepositAmountGrosze,
        Integer totalAmountGrosze
) {
}

