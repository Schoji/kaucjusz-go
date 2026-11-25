package com.kaucjuszGo.domain.receipt.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

class DinoStokrotkaReceiptParserTest {

    private final DinoStokrotkaReceiptParser parser = new DinoStokrotkaReceiptParser();

    @Test
    void shouldSupportDinoAndStokrotkaBarcodes() {
        assertTrue(parser.supports(new RawReceiptExtraction("9803770142779156695912550273", "CODE_128")));
        assertTrue(parser.supports(new RawReceiptExtraction("9809770142779156695901150273", "CODE_128")));
    }

    @Test
    void shouldNotSupportOtherOperatorBarcode() {
        assertFalse(parser.supports(new RawReceiptExtraction("201000702189501691150423", "CODE_128")));
    }

    @Test
    void shouldParseRealDinoBarcode() {
        var extraction = new RawReceiptExtraction("9803770142779156695912550273", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.DINO, result.operator());
        assertEquals(ReceiptScanStatus.ZAAKCEPTOWANY, result.status());
        assertEquals(12550, result.depositAmountGrosze());
        assertEquals(0, result.nonDepositAmountGrosze());
        assertEquals(12550, result.totalAmountGrosze());
    }

    @Test
    void shouldParseStokrotkaBarcode() {
        var extraction = new RawReceiptExtraction("9809770142779156695901150273", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.STOKROTKA, result.operator());
        assertEquals(ReceiptScanStatus.ZAAKCEPTOWANY, result.status());
        assertEquals(1150, result.depositAmountGrosze());
    }

    @Test
    void shouldSendToVerificationWhenFixedBlockDoesNotMatch() {
        var extraction = new RawReceiptExtraction("9803770142700056695912550273", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.DINO, result.operator());
        assertEquals(ReceiptScanStatus.DO_WERYFIKACJI, result.status());
        assertNull(result.depositAmountGrosze());
    }
}
