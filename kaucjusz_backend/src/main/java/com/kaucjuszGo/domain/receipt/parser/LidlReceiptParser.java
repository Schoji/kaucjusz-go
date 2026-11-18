package com.kaucjuszGo.domain.receipt.parser;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

@Component
public class LidlReceiptParser implements ReceiptParser {

    private static final Pattern LIDL_BARCODE_PATTERN =
            Pattern.compile("^2010(\\d{4})2(\\d{5})(\\d{10})$");

    @Override
    public boolean supports(RawReceiptExtraction extraction) {
        if (extraction == null || !extraction.hasBarcode()) {
            return false;
        }
        String barcode = extraction.rawBarcode();
        return barcode.length() == 24 && barcode.startsWith("2010");
    }

    @Override
    public ParsedReceipt parse(RawReceiptExtraction extraction) {
        String barcode = extraction.rawBarcode();
        var matcher = LIDL_BARCODE_PATTERN.matcher(barcode);

        if (!matcher.matches()) {
            return new ParsedReceipt(
                    RetailOperator.LIDL,
                    ReceiptScanStatus.DO_WERYFIKACJI,
                    barcode,
                    null,
                    null,
                    null
            );
        }

        int nonDepositGrosze = Integer.parseInt(matcher.group(1));
        int depositGrosze = Integer.parseInt(matcher.group(2));
        int totalGrosze = nonDepositGrosze + depositGrosze;

        ReceiptScanStatus status = depositGrosze > 0
                ? ReceiptScanStatus.ZAAKCEPTOWANY
                : ReceiptScanStatus.ODRZUCONY;

        return new ParsedReceipt(
                RetailOperator.LIDL,
                status,
                barcode,
                depositGrosze,
                nonDepositGrosze,
                totalGrosze
        );
    }
}

