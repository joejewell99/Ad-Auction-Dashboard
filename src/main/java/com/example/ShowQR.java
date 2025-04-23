package com.example;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.BitSource;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShowQR {
    private final ImageView imageView;

    public ShowQR(String otpAuthUrl, int size) throws Exception {
        this.imageView = new ImageView(createQRImage(otpAuthUrl, size));
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
    }

    public ImageView getImageView() {
        return imageView;
    }

    private Image createQRImage (String data, int size) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size);
        BufferedImage bufImg = MatrixToImageWriter.toBufferedImage(matrix);
        return SwingFXUtils.toFXImage(bufImg, null);
    }
}
