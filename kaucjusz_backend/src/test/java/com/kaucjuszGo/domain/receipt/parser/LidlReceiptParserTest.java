package com.kaucjuszGo.domain.receipt.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

class LidlReceiptParserTest {

    private final LidlReceiptParser parser = new LidlReceiptParser();

    @Test
    void shouldSupportValidLidlBarcode() {
        var extraction = new RawReceiptExtraction("201000702189501691150423", "CODE_128");
        assertTrue(parser.supports(extraction));
    }

    @Test
    void shouldNotSupportOtherOperatorBarcode() {
        var extraction = new RawReceiptExtraction("9803770142779156695912550273", "CODE_128");
        assertFalse(parser.supports(extraction));
    }

    @Test
    void shouldParseDepositAndNonDepositAmounts() {
        var extraction = new RawReceiptExtraction("201000702189501691150423", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.LIDL, result.operator());
        assertEquals(ReceiptScanStatus.ZAAKCEPTOWANY, result.status());
        assertEquals(18950, result.depositAmountGrosze());
        assertEquals(70, result.nonDepositAmountGrosze());
        assertEquals(19020, result.totalAmountGrosze());
    }

    @Test
    void shouldRejectReceiptWithZeroDepositAmount() {
        var extraction = new RawReceiptExtraction("201000102000001712165021", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.LIDL, result.operator());
        assertEquals(ReceiptScanStatus.ODRZUCONY, result.status());
        assertEquals(0, result.depositAmountGrosze());
        assertEquals(10, result.nonDepositAmountGrosze());
        assertEquals(10, result.totalAmountGrosze());
    }
}

