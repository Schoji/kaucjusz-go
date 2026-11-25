package com.kaucjuszGo.domain.receipt;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kaucjuszGo.domain.receipt.hash.ReceiptHasher;
import com.kaucjuszGo.domain.receipt.hash.ReceiptHasher.DualHashResult;
import com.kaucjuszGo.domain.receipt.parser.ParsedReceipt;
import com.kaucjuszGo.domain.receipt.parser.ReceiptParser;
import com.kaucjuszGo.domain.receipt.reader.RawReceiptExtraction;
import com.kaucjuszGo.domain.receipt.reader.ReceiptReader;

@Service
public class ReceiptAiAnalyzer {

    private final ReceiptReader receiptReader;
    private final List<ReceiptParser> receiptParsers;
    private final ReceiptHasher receiptHasher;

    public ReceiptAiAnalyzer(
            ReceiptReader receiptReader,
            List<ReceiptParser> receiptParsers,
            ReceiptHasher receiptHasher
    ) {
        this.receiptReader = receiptReader;
        this.receiptParsers = receiptParsers;
        this.receiptHasher = receiptHasher;
    }

    public record ReceiptAnalysisResult(
            RawReceiptExtraction extraction,
            ParsedReceipt parsedReceipt,
            DualHashResult hashes
    ) {
    }

    public ReceiptAnalysisResult analyze(InputStream imageStream) {
        RawReceiptExtraction extraction = receiptReader.extract(imageStream);

        ReceiptParser matchingParser = receiptParsers.stream()
                .filter(parser -> parser.supports(extraction))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No matching ReceiptParser configured"));

        ParsedReceipt parsed = matchingParser.parse(extraction);
        DualHashResult hashes = receiptHasher.computeHashes(parsed);

        return new ReceiptAnalysisResult(extraction, parsed, hashes);
    }
}
