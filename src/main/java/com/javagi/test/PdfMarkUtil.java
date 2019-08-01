package com.javagi.test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @Description pdf添加图片水印
 * @Author kuiwang
 * @Date 2019/7/8 9:54
 * @Version 1.0
 */
public class PdfMarkUtil {

    /**
     * 水印透明度 最小值为0，最大值为1
     */
    private static float alpha = 0.5f;
    /**
     * 水印图片旋转角度
     */
    private static double degree = 0f;

    private static int interval = 0;

    /**
     * 设置水印参数，不设置就使用默认值
     *
     * @param alpha    水印透明度
     * @param degree   水印图片旋转角度 *
     * @param interval 水印图片间隔
     */
    public static void setImageMarkOptions(float alpha, int degree,
                                           int interval) {
        if (alpha != 0.0f) {
            PdfMarkUtil.alpha = alpha;
        }
        if (degree != 0f) {
            PdfMarkUtil.degree = degree;
        }
        if (interval != 0f) {
            PdfMarkUtil.interval = interval;
        }

    }

    /**
     * 给图片添加水印图片
     *
     * @param waterImgPath 水印图片路径
     * @param srcImgPath   源图片路径
     * @param targerPath   目标图片路径
     */
    /*public static void waterMarkByImg(String waterImgPath, String srcImgPath,
                                      String targerPath, double degree) throws Exception {
        waterMarkByImg(waterImgPath, srcImgPath, targerPath, degree);
    }*/

    /**
     * 给图片添加水印图片
     *
     * @param waterImgPath 水印图片路径
     * @param srcImgPath   源图片路径
     * @param targerPath   目标图片路径
     */
    public static void waterMarkByImg(String waterImgPath, String srcImgPath, int degree) {
        try {
            waterMarkByImg(waterImgPath, srcImgPath, srcImgPath, degree);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加水印图片、可设置水印图片旋转角度
     *
     * @param waterImgPath 水印图片路径
     * @param srcImgPath   源图片路径
     * @param targerPath   目标图片路径
     * @param degree       水印图片旋转角度
     */
    public static void waterMarkByImg(String waterImgPath, String srcImgPath,
                                      String targerPath, int degree) throws Exception {
        OutputStream os = null;
        try {

            Image srcImg = ImageIO.read(new File(srcImgPath));

            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 1、得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 2、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 3、设置水印旋转
            if (0 != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2, (double) buffImg
                                .getHeight() / 2);
            }

            // 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(waterImgPath);

            // 5、得到Image对象。
            Image img = imgIcon.getImage();

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 6、水印图片的位置
            for (int height = interval + imgIcon.getIconHeight(); height < buffImg
                    .getHeight(); height = height + interval + imgIcon.getIconHeight()) {
                for (int weight = interval + imgIcon.getIconWidth(); weight < buffImg
                        .getWidth(); weight = weight + interval + imgIcon.getIconWidth()) {
                    g.drawImage(img, weight - imgIcon.getIconWidth(), height
                            - imgIcon.getIconHeight(), null);
                }
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            // 7、释放资源
            g.dispose();

            // 8、生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加水印图片");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception{

        /*System.out.println("..添加水印图片开始...");
        *//**
         * watermarkPath 水印图片地址 uploadPath 上传成功后文件地址
         *//*
        String watermarkPath = "F:/TEST/logo.jpg";
        String srcImgPath =  "F:/TEST/logo-01.jpg";
        String tarImgPath = "F:/TEST/logo-02.jpg";
        int degree = 330;   //旋转度数
        //修改默认参数
        ImageMarkUtil.setImageMarkOptions(0.08f, degree, 150);
        ImageMarkUtil.waterMarkByImg(watermarkPath, srcImgPath, tarImgPath, degree);
        System.out.println("..添加水印图片结束...");*/



        //读取原来的pdf
        PdfReader reader = new PdfReader("F:/TEST/品牌授权书-南极人-供应商_20190708.pdf");
        //生成以后的pdf
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream("F:/TEST/品牌授权书-水印.pdf"));
        int max = reader.getNumberOfPages();
        //max =2;
        // 文字水印
        for (int i = 1; i <= max; i++) {
            PdfContentByte over = stamp.getOverContent(i);
            over.beginText();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            over.setFontAndSize(bf, 16);
            over.setTextMatrix(10, 10);
            over.setColorFill(BaseColor.GRAY);
            over.showTextAligned(Element.ALIGN_LEFT, "java blog java-er.com ", 150,670, 0);
            // 0 0 0 表示左下脚 最后一个0是角度，0表示横着 45 表示斜着
            over.endText();
        }

        reader.close();
        stamp.close();



    }

}
