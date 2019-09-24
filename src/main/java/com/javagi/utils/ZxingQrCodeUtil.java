package com.javagi.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;

public class ZxingQrCodeUtil {

    private static final String QR_CODE_IMAGE_PATH = "D:/MyQRCode.png";

    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws Exception{
        if (text != null) {
            String format = "png";
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);
        }
    }

    public static void main(String[] args) throws Exception{
        try {
            generateQRCodeImage("客户名称：张三店铺\r\n"
                    +"名称：若成风店铺\r\n"
                    +"授权开始时间：2019-01-01\r\n"
                    +"授权开始时间：2019-12-31\r\n", 200, 200, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
        System.out.println("=========SUCCESS=========");
    }

}