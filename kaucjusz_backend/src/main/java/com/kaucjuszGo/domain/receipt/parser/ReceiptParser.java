package com.kaucjuszGo.domain.receipt.parser;

import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;

public interface ReceiptParser {

    boolean supports(RawReceiptExtraction extraction);

    ParsedReceipt parse(RawReceiptExtraction extraction);
}

