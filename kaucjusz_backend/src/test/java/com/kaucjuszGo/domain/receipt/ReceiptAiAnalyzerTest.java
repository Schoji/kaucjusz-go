package com.kaucjuszGo.domain.receipt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.kaucjuszGo.domain.receipt.hash.ReceiptHasher;
import com.kaucjuszGo.domain.receipt.parser.BiedronkaReceiptParser;
import com.kaucjuszGo.domain.receipt.parser.DinoStokrotkaReceiptParser;
import com.kaucjuszGo.domain.receipt.parser.FallbackReceiptParser;
import com.kaucjuszGo.domain.receipt.parser.LidlReceiptParser;
import com.kaucjuszGo.domain.receipt.reader.BarcodeOnlyReceiptReader;

class ReceiptAiAnalyzerTest {

    private final ReceiptAiAnalyzer analyzer = new ReceiptAiAnalyzer(
            new BarcodeOnlyReceiptReader(),
            List.of(
                    new LidlReceiptParser(),
                    new BiedronkaReceiptParser(),
                    new DinoStokrotkaReceiptParser(),
                    new FallbackReceiptParser()
            ),
            new ReceiptHasher()
    );

    @Test
    void shouldAnalyzeLidlReceiptImageEndToEnd() throws Exception {
        byte[] png = generateBarcodePng("201000702189501691150423");

        var result = analyzer.analyze(new ByteArrayInputStream(png));

        assertEquals(RetailOperator.LIDL, result.parsedReceipt().operator());
        assertEquals(ReceiptScanStatus.ZAAKCEPTOWANY, result.parsedReceipt().status());
        assertEquals(18950, result.parsedReceipt().depositAmountGrosze());
        assertEquals(70, result.parsedReceipt().nonDepositAmountGrosze());
        assertEquals(19020, result.parsedReceipt().totalAmountGrosze());
        assertNotNull(result.hashes().barcodeHash());
        assertNotNull(result.hashes().receiptFingerprintHash());
    }

    @Test
    void shouldAnalyzeBiedronkaReceiptImageEndToEnd() throws Exception {
        byte[] png = generateBarcodePng("9866090138461179239714703859");

        var result = analyzer.analyze(new ByteArrayInputStream(png));

        assertEquals(RetailOperator.BIEDRONKA, result.parsedReceipt().operator());
        assertEquals(ReceiptScanStatus.ZAAKCEPTOWANY, result.parsedReceipt().status());
        assertEquals(3850, result.parsedReceipt().depositAmountGrosze());
        assertNotNull(result.hashes().barcodeHash());
    }

    @Test
    void shouldAnalyzeKauflandReceiptImageAsFallbackForVerification() throws Exception {
        byte[] png = generateBarcodePng("9670484904184040068001027286");

        var result = analyzer.analyze(new ByteArrayInputStream(png));

        assertEquals(RetailOperator.KAUFLAND, result.parsedReceipt().operator());
        assertEquals(ReceiptScanStatus.DO_WERYFIKACJI, result.parsedReceipt().status());
        assertNull(result.parsedReceipt().depositAmountGrosze());
        assertNotNull(result.hashes().barcodeHash());
        assertNotNull(result.hashes().receiptFingerprintHash());
    }

    @Test
    void shouldHandleUnreadableImageWithoutCrashing() throws Exception {
        BufferedImage blank = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = blank.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 200, 100);
        g2d.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(blank, "png", out);

        var result = analyzer.analyze(new ByteArrayInputStream(out.toByteArray()));

        assertEquals(RetailOperator.UNKNOWN, result.parsedReceipt().operator());
        assertEquals(ReceiptScanStatus.DO_WERYFIKACJI, result.parsedReceipt().status());
        assertNull(result.hashes().barcodeHash());
        assertNull(result.hashes().receiptFingerprintHash());
    }

    private static byte[] generateBarcodePng(String digits) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(digits, BarcodeFormat.CODE_128, 600, 160);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }
}
