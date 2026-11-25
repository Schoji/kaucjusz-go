package com.kaucjuszGo.domain.receipt.hash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.hash.ReceiptHasher.DualHashResult;
import com.kaucjuszGo.domain.receipt.parser.ParsedReceipt;

class ReceiptHasherTest {

    private final ReceiptHasher hasher = new ReceiptHasher();

    @Test
    void shouldComputeDeterministicDualHashesForLidlReceipt() {
        var parsed = new ParsedReceipt(
                RetailOperator.LIDL,
                ReceiptScanStatus.ZAAKCEPTOWANY,
                "201000702189501691150423",
                18950,
                70,
                19020
        );

        DualHashResult first = hasher.computeHashes(parsed);
        DualHashResult second = hasher.computeHashes(parsed);

        assertNotNull(first.barcodeHash());
        assertNotNull(first.receiptFingerprintHash());
        assertEquals(64, first.barcodeHash().length());
        assertEquals(64, first.receiptFingerprintHash().length());
        assertEquals(first.barcodeHash(), second.barcodeHash());
        assertEquals(first.receiptFingerprintHash(), second.receiptFingerprintHash());
        assertEquals(
                "LIDL|201000702189501691150423|18950|70|19020|NULL|NULL|NULL|NULL|NULL|NULL|NULL",
                first.canonicalPayload()
        );
    }

    @Test
    void shouldNormalizeWhitespaceInBarcode() {
        var clean = new ParsedReceipt(
                RetailOperator.LIDL,
                ReceiptScanStatus.ZAAKCEPTOWANY,
                "201000702189501691150423",
                18950,
                70,
                19020
        );
        var withSpaces = new ParsedReceipt(
                RetailOperator.LIDL,
                ReceiptScanStatus.ZAAKCEPTOWANY,
                " 2010 00702189501691150423 ",
                18950,
                70,
                19020
        );

        DualHashResult cleanResult = hasher.computeHashes(clean);
        DualHashResult spacedResult = hasher.computeHashes(withSpaces);

        assertEquals(cleanResult.barcodeHash(), spacedResult.barcodeHash());
        assertEquals(cleanResult.receiptFingerprintHash(), spacedResult.receiptFingerprintHash());
    }

    @Test
    void shouldProduceDifferentFingerprintWhenAmountChanges() {
        var original = new ParsedReceipt(
                RetailOperator.LIDL,
                ReceiptScanStatus.ZAAKCEPTOWANY,
                "201000702189501691150423",
                18950,
                70,
                19020
        );
        var tampered = new ParsedReceipt(
                RetailOperator.LIDL,
                ReceiptScanStatus.ZAAKCEPTOWANY,
                "201000702189501691150423",
                20000,
                70,
                20070
        );

        DualHashResult originalResult = hasher.computeHashes(original);
        DualHashResult tamperedResult = hasher.computeHashes(tampered);

        assertEquals(originalResult.barcodeHash(), tamperedResult.barcodeHash());
        assertNotEquals(originalResult.receiptFingerprintHash(), tamperedResult.receiptFingerprintHash());
    }

    @Test
    void shouldComputeHashesForFallbackReceiptWithBarcodeOnly() {
        var kaufland = new ParsedReceipt(
                RetailOperator.KAUFLAND,
                ReceiptScanStatus.DO_WERYFIKACJI,
                "8670484904184040088001027286",
                null,
                null,
                null
        );

        DualHashResult result = hasher.computeHashes(kaufland);

        assertNotNull(result.barcodeHash());
        assertNotNull(result.receiptFingerprintHash());
        assertEquals(
                "KAUFLAND|8670484904184040088001027286|NULL|NULL|NULL|NULL|NULL|NULL|NULL|NULL|NULL|NULL",
                result.canonicalPayload()
        );
    }

    @Test
    void shouldReturnNullHashesWhenNoIdentifiableDataPresent() {
        var unreadable = new ParsedReceipt(
                RetailOperator.UNKNOWN,
                ReceiptScanStatus.DO_WERYFIKACJI,
                null,
                null,
                null,
                null
        );

        DualHashResult result = hasher.computeHashes(unreadable);

        assertNull(result.barcodeHash());
        assertNull(result.receiptFingerprintHash());
    }
}
