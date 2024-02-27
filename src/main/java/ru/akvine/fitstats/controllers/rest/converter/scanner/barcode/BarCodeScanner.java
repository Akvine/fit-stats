package ru.akvine.fitstats.controllers.rest.converter.scanner.barcode;

import com.google.common.base.Preconditions;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.enums.BarCodeType;
import ru.akvine.fitstats.exceptions.barcode.BarCodeScanException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class BarCodeScanner {
    public BarCodeScanResult scan(MultipartFile photo) {
        try {
            return scan(photo.getInputStream());
        } catch (Exception exception) {
            throw new BarCodeScanException("Bar code scan error, can't get input stream from photo = " + exception.getMessage());
        }
    }

    public BarCodeScanResult scan(byte[] file) {
        return scan(new ByteArrayInputStream(file));
    }

    public BarCodeScanResult scan(InputStream file) {
        Preconditions.checkNotNull(file, "file is null");

        try {
            BufferedImage image = ImageIO.read(file);

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(image)));

            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);

            return new BarCodeScanResult()
                    .setNumber(result.getText())
                    .setBarCodeType(BarCodeType.valueOf(result.getBarcodeFormat().toString()));
        } catch (Exception exception) {
            throw new BarCodeScanException("Bar code scan error = " + exception.getMessage());
        }
    }
}
