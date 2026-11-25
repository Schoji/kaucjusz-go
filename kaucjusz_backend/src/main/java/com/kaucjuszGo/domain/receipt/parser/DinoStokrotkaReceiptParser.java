package com.kaucjuszGo.domain.receipt.parser;

import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

@Component
@Order(30)
public class DinoStokrotkaReceiptParser implements ReceiptParser {

    private static final Pattern DINO_STOKROTKA_BARCODE_PATTERN =
            Pattern.compile("^(980377|980977)\\d{5}791\\d{6}(\\d{5})\\d{3}$");

    @Override
    public boolean supports(RawReceiptExtraction extraction) {
        if (extraction == null || !extraction.hasBarcode()) {
            return false;
        }
        String barcode = extraction.rawBarcode();
        return barcode.length() == 28
                && (barcode.startsWith("980377") || barcode.startsWith("980977"));
    }

    @Override
    public ParsedReceipt parse(RawReceiptExtraction extraction) {
        String barcode = extraction.rawBarcode();
        RetailOperator operator = barcode.startsWith("980377")
                ? RetailOperator.DINO
                : RetailOperator.STOKROTKA;

        var matcher = DINO_STOKROTKA_BARCODE_PATTERN.matcher(barcode);
        if (!matcher.matches()) {
            return new ParsedReceipt(
                    operator,
                    ReceiptScanStatus.DO_WERYFIKACJI,
                    barcode,
                    null,
                    null,
                    null
            );
        }

        int depositGrosze = Integer.parseInt(matcher.group(2));

        ReceiptScanStatus status = depositGrosze > 0
                ? ReceiptScanStatus.ZAAKCEPTOWANY
                : ReceiptScanStatus.ODRZUCONY;

        return new ParsedReceipt(
                operator,
                status,
                barcode,
                depositGrosze,
                0,
                depositGrosze
        );
    }
}
