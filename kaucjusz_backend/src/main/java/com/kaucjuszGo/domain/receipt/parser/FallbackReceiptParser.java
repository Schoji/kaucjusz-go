package com.kaucjuszGo.domain.receipt.parser;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class FallbackReceiptParser implements ReceiptParser {

    @Override
    public boolean supports(RawReceiptExtraction extraction) {
        return true;
    }

    @Override
    public ParsedReceipt parse(RawReceiptExtraction extraction) {
        if (extraction == null || !extraction.hasBarcode()) {
            return new ParsedReceipt(
                    RetailOperator.UNKNOWN,
                    ReceiptScanStatus.DO_WERYFIKACJI,
                    null,
                    null,
                    null,
                    null
            );
        }

        String barcode = extraction.rawBarcode();
        RetailOperator operator = (barcode.length() == 28 && barcode.startsWith("86704849041840"))
                ? RetailOperator.KAUFLAND
                : RetailOperator.UNKNOWN;

        return new ParsedReceipt(
                operator,
                ReceiptScanStatus.DO_WERYFIKACJI,
                barcode,
                null,
                null,
                null
        );
    }
}
