package com.kaucjuszGo.domain.receipt.reader;

public record RawReceiptExtraction(
        String rawBarcode,
        String barcodeFormat
) {
    public boolean hasBarcode() {
        return rawBarcode != null && !rawBarcode.isBlank();
    }
}

