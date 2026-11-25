package com.kaucjuszGo.domain.receipt.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

class FallbackReceiptParserTest {

    private final FallbackReceiptParser parser = new FallbackReceiptParser();

    @Test
    void shouldAlwaysSupportAnyExtraction() {
        assertTrue(parser.supports(new RawReceiptExtraction("8670484904184040088001027286", "CODE_128")));
        assertTrue(parser.supports(new RawReceiptExtraction(null, null)));
        assertTrue(parser.supports(null));
    }

    @Test
    void shouldRecognizeKauflandAndSendToVerification() {
        var extraction = new RawReceiptExtraction("8670484904184040088001027286", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.KAUFLAND, result.operator());
        assertEquals(ReceiptScanStatus.DO_WERYFIKACJI, result.status());
        assertEquals("8670484904184040088001027286", result.rawBarcode());
        assertNull(result.depositAmountGrosze());
    }

    @Test
    void shouldMarkUnknownBarcodeForVerification() {
        var extraction = new RawReceiptExtraction("9341206925861179264504101805", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.UNKNOWN, result.operator());
        assertEquals(ReceiptScanStatus.DO_WERYFIKACJI, result.status());
        assertEquals("9341206925861179264504101805", result.rawBarcode());
        assertNull(result.depositAmountGrosze());
    }

    @Test
    void shouldHandleMissingBarcodeGracefully() {
        ParsedReceipt result = parser.parse(new RawReceiptExtraction(null, null));

        assertEquals(RetailOperator.UNKNOWN, result.operator());
        assertEquals(ReceiptScanStatus.DO_WERYFIKACJI, result.status());
        assertNull(result.rawBarcode());
        assertNull(result.depositAmountGrosze());
    }
}
