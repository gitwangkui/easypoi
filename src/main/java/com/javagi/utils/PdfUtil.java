package com.javagi.utils;

import cn.afterturn.easypoi.view.PoiBaseView;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 根据pdf模板导出pdf
 *          参考：https://www.cnblogs.com/wangpeng00700/p/8418594.html
 * @Author kuiwang
 * @Date 2019/7/1 20:21
 * @Version 1.0
 */
public class PdfUtil extends PoiBaseView {

    // 文件格式
    public static final String EXPORT_SUFFIX_PDF = ".pdf";
    // 传入文字map数据
    public static final String DATA_CHAR_MAP = "charMap";
    // 传入图片map数据
    public static final String DATA_IMG_MAP = "imgMap";

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_UTF8 = "UTF8";
    public static final String ENCODING_ISO = "ISO-8859-1";

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    /**
     * @Description 根据模板导出pdf文件, 生成后直接导出下载
     * @Author kuiwang
     * @Date 14:26 2019/7/2
     * @param pdfTempLatePath   pdf模板路径
     * @param fileName  生成pdf文件名称
     * @param map  生成pdf的数据
     * @param request
     * @param response
     * @Return
     */
    public static void exportPdfTemplate(String pdfTempLatePath, String fileName, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {

        // 指定解析器
        ServletOutputStream os = null;
        PdfStamper ps = null;
        PdfReader reader = null;
        Document doc = null;

        try {
            if (isIE(request)) {
                fileName = URLEncoder.encode(fileName, ENCODING_UTF8);
            } else {
                fileName = new String(fileName.getBytes(ENCODING_UTF_8), ENCODING_ISO);
            }
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + EXPORT_SUFFIX_PDF);
            os = response.getOutputStream();
            // 2 读取pdf模板表单
            reader = new PdfReader(pdfTempLatePath);
            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);
            doc = new Document();
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示(使用iTextAsian.jar中的字体)
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.EMBEDDED);
            //BaseFont bf  = BaseFont.createFont("/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(bf);
            // 6遍历data 给pdf表单表格赋值
            if (!map.isEmpty()) {
                if (map.containsKey(DATA_CHAR_MAP)) {
                    Map<String, String> datemap = (Map<String, String>) map.get(DATA_CHAR_MAP);
                    for (String key : datemap.keySet()) {
                        String value = datemap.get(key);
                        form.setField(key, value);
                    }
                    // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
                    ps.setFormFlattening(true);
                }

                // 图片类的内容处理
                if (map.containsKey(DATA_IMG_MAP)) {
                    Map<String, String> imgmap = (Map<String, String>) map.get(DATA_IMG_MAP);
                    for (String key : imgmap.keySet()) {
                        String value = imgmap.get(key);
                        String imgpath = value;
                        int pageNo = form.getFieldPositions(key).get(0).page;
                        Rectangle signRect = form.getFieldPositions(key).get(0).position;
                        float x = signRect.getLeft();
                        float y = signRect.getBottom();
                        // 根据路径读取图片
                        Image image = Image.getInstance(imgpath);
                        // 获取图片页面
                        PdfContentByte under = ps.getOverContent(pageNo);
                        // 图片大小自适应
                        image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                        // 添加图片
                        image.setAbsolutePosition(x, y);
                        under.addImage(image);
                    }
                    // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
                    ps.setFormFlattening(true);
                }
            }
            ps.close();

            PdfCopy copy = new PdfCopy(doc, os);
            doc.open();
            // 支持多页
            PdfImportedPage importPage = null;
            //判断工单是否存在附件
            int attrCount = Integer.parseInt(map.get("attrcount").toString());
            if (attrCount > 0) {
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    importPage = copy.getImportedPage(new PdfReader(pdfTempLatePath), i);
                    copy.addPage(importPage);
                }
            } else {
                importPage = copy.getImportedPage(new PdfReader(pdfTempLatePath), 1);
                copy.addPage(importPage);
            }
            doc.close();
            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============" + e);
        } finally {
            try {
                ps.close();
                reader.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description 根据模板导出pdf文件, 生成到指定路径
     * @Author kuiwang
     * @Date 15:39 2019/7/2
     * @param o 传入的数据map
     * @param templatePath  pdf模板
     * @param newPDFPath    新生成的pdf路径
     * @Return
     */
    public static void exportPdf(Map<String,Object> o, String templatePath, String newPDFPath) {
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;

        try {
            // 给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.EMBEDDED);
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bf);
            //文字类的内容处理
            if (!o.isEmpty() && o.containsKey(DATA_CHAR_MAP)) {
                Map<String,String> datemap = (Map<String,String>)o.get(DATA_CHAR_MAP);
                for(String key : datemap.keySet()){
                    String value = datemap.get(key);
                    form.setField(key,value);
                }
                // 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
                stamper.setFormFlattening(true);
            }

            // 图片类的内容处理
            if (!o.isEmpty() && o.containsKey(DATA_IMG_MAP)) {
                Map<String, String> imgmap = (Map<String, String>) o.get(DATA_IMG_MAP);
                for (String key : imgmap.keySet()) {
                    String value = imgmap.get(key);
                    String imgpath = value;
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    // 根据路径读取图片
                    Image image = Image.getInstance(imgpath);
                    // 获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    // 图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    // 添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
                stamper.setFormFlattening(true);
            }
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        // 文字
        Map<String,String> map = new HashMap<>();
        map.put("id","王谋仁");
        map.put("one","2018年1月1日");
        map.put("two","晴朗");
        map.put("zq_xm","打羽毛球");
        map.put("cm_xm","打羽毛球");
        map.put("tj_xm","打羽毛球");

        // 图片
        //Map<String,String> map2 =new HashMap<>();
        //map2.put("img","c:/50336.jpg");

        Map<String,Object> data=new HashMap();
        data.put("charMap",map);
        //data.put("imgMap",map2);

        String templatePath = "doc/wordToPdf.pdf";
        String newPDFPath = "F:/TEST/新pdf名称-test.pdf";
        exportPdf(data, templatePath, newPDFPath);
        System.out.println("生成pdf完成！ 耗时：" + (System.currentTimeMillis() - l) + " ms");
    }


}
