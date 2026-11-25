package com.kaucjuszGo.domain.receipt.parser;

import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

@Component
@Order(20)
public class BiedronkaReceiptParser implements ReceiptParser {

    private static final Pattern BIEDRONKA_BARCODE_PATTERN =
            Pattern.compile("^9866\\d{19}(\\d{4})\\d$");

    @Override
    public boolean supports(RawReceiptExtraction extraction) {
        if (extraction == null || !extraction.hasBarcode()) {
            return false;
        }
        String barcode = extraction.rawBarcode();
        return barcode.length() == 28 && barcode.startsWith("9866");
    }

    @Override
    public ParsedReceipt parse(RawReceiptExtraction extraction) {
        String barcode = extraction.rawBarcode();
        var matcher = BIEDRONKA_BARCODE_PATTERN.matcher(barcode);

        if (!matcher.matches()) {
            return new ParsedReceipt(
                    RetailOperator.BIEDRONKA,
                    ReceiptScanStatus.DO_WERYFIKACJI,
                    barcode,
                    null,
                    null,
                    null
            );
        }

        int depositGrosze = Integer.parseInt(matcher.group(1)) * 10;

        ReceiptScanStatus status = depositGrosze > 0
                ? ReceiptScanStatus.ZAAKCEPTOWANY
                : ReceiptScanStatus.ODRZUCONY;

        return new ParsedReceipt(
                RetailOperator.BIEDRONKA,
                status,
                barcode,
                depositGrosze,
                0,
                depositGrosze
        );
    }
}
