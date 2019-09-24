package com.javagi.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;
/**
 * @Description https://www.cnblogs.com/Marydon20170307/p/9546411.html
 * @Author kuiwang
 * @Date 11:25 2019/9/24
 * @Return
 */
public class ZxingQrCodeUtil {

    private static final String QR_CODE_IMAGE_PATH = "D:/MyQRCode.png";

    /**
     * @Description 生成二维码
     * @Author kuiwang
     * @Date 11:29 2019/9/24
     * @param text
     * @param width
     * @param height
     * @param filePath
     * @Return
     */
    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws Exception{
        if (text != null) {
            BitMatrix bitMatrix = null;
            String format = "png";
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 0);
            //hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            // 去除白边
            bitMatrix = deleteWhite(bitMatrix);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);
        }
    }

    /**
     * @Description 去除白边
     * @Author kuiwang
     * @Date 11:30 2019/9/24
     * @param matrix
     * @Return
     */
    private static BitMatrix deleteWhite(BitMatrix matrix){
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] +1;
        int resHeight = rec[3] +1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();

        for (int i = 0; i < resWidth ; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j+ rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }

        int width = resMatrix.getWidth();
        int height = resMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, resMatrix.get(x, y) ? 0 : 255);
            }
        }
        return resMatrix;
    }

    public static void main(String[] args) throws Exception{
        try {
            generateQRCodeImage("客户名称：张三店铺\r\n"
                    +"名称：若成风店铺\r\n"
                    +"授权开始时间：2019-01-01\r\n"
                    +"授权开始时间：2019-12-31\r\n", 120, 120, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
        System.out.println("=========SUCCESS=========");
    }

}