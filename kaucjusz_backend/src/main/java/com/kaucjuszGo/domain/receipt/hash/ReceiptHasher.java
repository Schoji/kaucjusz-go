package com.kaucjuszGo.domain.receipt.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.kaucjuszGo.domain.receipt.parser.ParsedReceipt;

@Component
public class ReceiptHasher {

    private static final String NULL_TOKEN = "NULL";
    private static final String DELIMITER = "|";

    public record DualHashResult(
            String barcodeHash,
            String receiptFingerprintHash,
            String canonicalPayload
    ) {
    }

    public DualHashResult computeHashes(ParsedReceipt parsed) {
        if (parsed == null) {
            return new DualHashResult(null, null, null);
        }

        String normalizedBarcode = normalizeString(parsed.rawBarcode());
        String barcodeHash = normalizedBarcode != null
                ? sha256Hex(normalizedBarcode)
                : null;

        String canonicalPayload = buildCanonicalFingerprintPayload(parsed);
        boolean hasIdentifiableData = normalizedBarcode != null
                || parsed.depositAmountGrosze() != null
                || parsed.totalAmountGrosze() != null;

        String fingerprintHash = hasIdentifiableData
                ? sha256Hex(canonicalPayload)
                : null;

        return new DualHashResult(barcodeHash, fingerprintHash, canonicalPayload);
    }

    public String buildCanonicalFingerprintPayload(ParsedReceipt parsed) {
        return String.join(DELIMITER,
                tokenOrNull(parsed.operator() != null ? parsed.operator().name() : null),
                tokenOrNull(normalizeString(parsed.rawBarcode())),
                tokenOrNull(parsed.depositAmountGrosze()),
                tokenOrNull(parsed.nonDepositAmountGrosze()),
                tokenOrNull(parsed.totalAmountGrosze()),
                NULL_TOKEN, // total_items_count (V2)
                NULL_TOKEN, // canonical_items_list (V2)
                NULL_TOKEN, // printed_at_iso (V2)
                NULL_TOKEN, // valid_until_iso (V2)
                NULL_TOKEN, // machine_id (V2)
                NULL_TOKEN, // store_id (V2)
                NULL_TOKEN  // voucher_number (V2)
        );
    }

    private static String normalizeString(String value) {
        if (value == null) {
            return null;
        }
        String cleaned = value.trim().replaceAll("\\s+", "");
        return cleaned.isEmpty() ? null : cleaned.toUpperCase(Locale.ROOT);
    }

    private static String tokenOrNull(Object value) {
        if (value == null) {
            return NULL_TOKEN;
        }
        String text = value.toString().trim();
        return text.isEmpty() ? NULL_TOKEN : text;
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
