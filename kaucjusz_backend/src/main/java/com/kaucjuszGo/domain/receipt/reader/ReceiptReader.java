package com.kaucjuszGo.domain.receipt.reader;

import java.io.InputStream;

public interface ReceiptReader {

    RawReceiptExtraction extract(InputStream imageStream);
}
