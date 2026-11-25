package com.kaucjuszGo.domain.receipt.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.kaucjuszGo.domain.receipt.ReceiptScanStatus;
import com.kaucjuszGo.domain.receipt.RetailOperator;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

class BiedronkaReceiptParserTest {

    private final BiedronkaReceiptParser parser = new BiedronkaReceiptParser();

    @Test
    void shouldSupportValidBiedronkaBarcode() {
        var extraction = new RawReceiptExtraction("9866090138461179239714703859", "CODE_128");
        assertTrue(parser.supports(extraction));
    }

    @Test
    void shouldNotSupportOtherOperatorBarcode() {
        var extraction = new RawReceiptExtraction("201000702189501691150423", "CODE_128");
        assertFalse(parser.supports(extraction));
    }

    @Test
    void shouldParseDepositAmountFromBiedronkaBarcode() {
        var extraction = new RawReceiptExtraction("9866090138461179239714703859", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.BIEDRONKA, result.operator());
        assertEquals(ReceiptScanStatus.ZAAKCEPTOWANY, result.status());
        assertEquals(3850, result.depositAmountGrosze());
        assertEquals(0, result.nonDepositAmountGrosze());
        assertEquals(3850, result.totalAmountGrosze());
    }

    @Test
    void shouldParseSecondRealBiedronkaBarcode() {
        var extraction = new RawReceiptExtraction("9866109025441179223250902706", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.BIEDRONKA, result.operator());
        assertEquals(ReceiptScanStatus.ZAAKCEPTOWANY, result.status());
        assertEquals(2700, result.depositAmountGrosze());
    }

    @Test
    void shouldRejectReceiptWithZeroDepositAmount() {
        var extraction = new RawReceiptExtraction("9866090138461179239714700009", "CODE_128");

        ParsedReceipt result = parser.parse(extraction);

        assertEquals(RetailOperator.BIEDRONKA, result.operator());
        assertEquals(ReceiptScanStatus.ODRZUCONY, result.status());
        assertEquals(0, result.depositAmountGrosze());
    }
}
