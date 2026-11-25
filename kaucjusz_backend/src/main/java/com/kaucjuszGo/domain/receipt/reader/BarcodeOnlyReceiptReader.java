package com.kaucjuszGo.domain.receipt.reader;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

@Component
public class BarcodeOnlyReceiptReader implements ReceiptReader {

    private static final Logger log = LoggerFactory.getLogger(BarcodeOnlyReceiptReader.class);
    private static final int[] ROTATION_ANGLES = {0, 90, 180, 270};

    private final Map<DecodeHintType, Object> decodeHints;

    public BarcodeOnlyReceiptReader() {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, List.of(
                BarcodeFormat.CODE_128,
                BarcodeFormat.ITF,
                BarcodeFormat.EAN_13,
                BarcodeFormat.EAN_8,
                BarcodeFormat.CODE_39,
                BarcodeFormat.CODABAR
        ));
        this.decodeHints = Map.copyOf(hints);
    }

    @Override
    public RawReceiptExtraction extract(InputStream imageStream) {
        if (imageStream == null) {
            return new RawReceiptExtraction(null, null);
        }

        final BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(imageStream);
        } catch (IOException e) {
            log.warn("Failed to read receipt image stream", e);
            return new RawReceiptExtraction(null, null);
        }

        if (originalImage == null) {
            log.warn("Uploaded file is not a supported image format");
            return new RawReceiptExtraction(null, null);
        }

        for (int angle : ROTATION_ANGLES) {
            BufferedImage candidate = (angle == 0) ? originalImage : rotateImage(originalImage, angle);
            Optional<Result> decoded = tryDecodeBarcode(candidate);
            if (decoded.isPresent()) {
                Result result = decoded.get();
                String normalized = normalizeBarcodeText(result.getText());
                String format = result.getBarcodeFormat() != null
                        ? result.getBarcodeFormat().name()
                        : "UNKNOWN";
                log.debug("Decoded barcode [format={}, rotation={}]: {}", format, angle, normalized);
                return new RawReceiptExtraction(normalized, format);
            }
        }

        log.info("No barcode found in receipt image at 0/90/180/270 degrees");
        return new RawReceiptExtraction(null, null);
    }

    private Optional<Result> tryDecodeBarcode(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);

        Optional<Result> hybridResult = decodeWithBitmap(new BinaryBitmap(new HybridBinarizer(source)));
        if (hybridResult.isPresent()) {
            return hybridResult;
        }

        return decodeWithBitmap(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
    }

    private Optional<Result> decodeWithBitmap(BinaryBitmap bitmap) {
        MultiFormatReader reader = new MultiFormatReader();
        reader.setHints(decodeHints);
        try {
            return Optional.ofNullable(reader.decodeWithState(bitmap));
        } catch (NotFoundException e) {
            return Optional.empty();
        } finally {
            reader.reset();
        }
    }

    private static String normalizeBarcodeText(String raw) {
        if (raw == null) {
            return null;
        }
        return raw.trim().replaceAll("\\s+", "");
    }

    private static BufferedImage rotateImage(BufferedImage src, int degrees) {
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.floor(src.getWidth() * cos + src.getHeight() * sin);
        int newHeight = (int) Math.floor(src.getHeight() * cos + src.getWidth() * sin);

        int imageType = src.getType() == BufferedImage.TYPE_CUSTOM
                ? BufferedImage.TYPE_INT_RGB
                : src.getType();
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, imageType);
        Graphics2D g2d = rotated.createGraphics();
        try {
            AffineTransform at = new AffineTransform();
            at.translate((newWidth - src.getWidth()) / 2.0, (newHeight - src.getHeight()) / 2.0);
            at.rotate(radians, src.getWidth() / 2.0, src.getHeight() / 2.0);
            g2d.setTransform(at);
            g2d.drawImage(src, 0, 0, null);
        } finally {
            g2d.dispose();
        }
        return rotated;
    }
}
