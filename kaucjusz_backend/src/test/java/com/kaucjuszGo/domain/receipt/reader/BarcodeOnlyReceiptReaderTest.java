package com.kaucjuszGo.domain.receipt.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

class BarcodeOnlyReceiptReaderTest {

    private final BarcodeOnlyReceiptReader reader = new BarcodeOnlyReceiptReader();

    @Test
    void shouldDecodeCode128BarcodeAt0Degrees() throws Exception {
        String code = "201000702189501691150423";
        byte[] png = generateBarcodePng(code, 0);

        RawReceiptExtraction result = reader.extract(new ByteArrayInputStream(png));

        assertTrue(result.hasBarcode());
        assertEquals(code, result.rawBarcode());
        assertEquals("CODE_128", result.barcodeFormat());
    }

    @Test
    void shouldDecodeRotatedBarcodeAt90And270Degrees() throws Exception {
        String code = "9803770142779156695912550273";

        RawReceiptExtraction rotated90 = reader.extract(new ByteArrayInputStream(generateBarcodePng(code, 90)));
        RawReceiptExtraction rotated270 = reader.extract(new ByteArrayInputStream(generateBarcodePng(code, 270)));

        assertEquals(code, rotated90.rawBarcode());
        assertEquals(code, rotated270.rawBarcode());
    }

    @Test
    void shouldReturnEmptyExtractionForBlankImage() throws Exception {
        BufferedImage blank = new BufferedImage(300, 150, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = blank.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 300, 150);
        g2d.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(blank, "png", out);

        RawReceiptExtraction result = reader.extract(new ByteArrayInputStream(out.toByteArray()));

        assertFalse(result.hasBarcode());
        assertNull(result.rawBarcode());
        assertNull(result.barcodeFormat());
    }

    private static byte[] generateBarcodePng(String digits, int rotateDegrees) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(digits, BarcodeFormat.CODE_128, 600, 160);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        if (rotateDegrees != 0) {
            image = rotate(image, rotateDegrees);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    private static BufferedImage rotate(BufferedImage src, int degrees) {
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newW = (int) Math.floor(src.getWidth() * cos + src.getHeight() * sin);
        int newH = (int) Math.floor(src.getHeight() * cos + src.getWidth() * sin);
        BufferedImage rotated = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, newW, newH);
        AffineTransform at = new AffineTransform();
        at.translate((newW - src.getWidth()) / 2.0, (newH - src.getHeight()) / 2.0);
        at.rotate(radians, src.getWidth() / 2.0, src.getHeight() / 2.0);
        g2d.setTransform(at);
        g2d.drawImage(src, 0, 0, null);
        g2d.dispose();
        return rotated;
    }
}
