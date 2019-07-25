package com.javagi.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.pdf.PdfExportUtil;
import cn.afterturn.easypoi.pdf.entity.PdfExportParams;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.afterturn.easypoi.word.WordExportUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**@Description
 *  该工具类包含了导出excel、word、pdf。
 *  excel和word导出主要使用了esaypoi。其中的指令参考官网教程：http://easypoi.mydoc.io/
 *  pdf主要使用了esaypoi生成word，再利用spire合成后生成pdf，再利用pdfbox去除第一页中水印
 *
 *  excel分为三种：
 *      普通导出excel
 *      根据模板导出excel(推荐)
 *      根据模板合并单元格导出excel(推荐)
 *  word
 *      根据word模板导出word文档(推荐)
 *  pdf分为三种：
 *      普通导出pdf
 *      根据pdf模板导出pdf
 *      根据word模板生成word在转成pdf文档(推荐)
 * @Author kuiwang
 * @Date 2019/2/19 9:45
 * @Description: 导出方法包装类
 */
public class ExportUtil extends PoiBaseView {

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_UTF8 = "UTF8";
    public static final String ENCODING_ISO = "ISO-8859-1";

    public static final String EXPORT_SUFFIX_EXCEL = ".xls";
    public static final String EXPORT_SUFFIX_PDF = ".pdf";
    public static final String EXPORT_SUFFIX_DOCX = ".docx";

    // 传入文字map数据
    public static final String DATA_CHAR_MAP = "charMap";
    // 传入图片map数据
    public static final String DATA_IMG_MAP = "imgMap";

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
    }

    /**
     * @Description 导出excel
     * @Author kuiwang
     * @Date 10:37 2019/7/25
     * @param title 标题
     * @param fileName 文件名称
     * @param pojoClass 实体类  **.class
     * @param dataSet   数据集合
     * @param request
     * @param response
     * @Return
     */
    protected void writeToExcel(String title, String fileName, Class<?> pojoClass, Collection<?> dataSet, HttpServletRequest request, HttpServletResponse response) {
        try {
        ExportParams params = new ExportParams(title, "测试", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, pojoClass, dataSet);
        if (isIE(request)) {
            fileName = URLEncoder.encode(fileName,ENCODING_UTF8);
        } else {
            fileName = new String(fileName.getBytes(ENCODING_UTF_8),ENCODING_ISO);
        }
        response.setHeader("content-disposition", "attachment;filename=" + fileName +EXPORT_SUFFIX_EXCEL);
        response.setContentType("application/octet-stream");
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        } catch (UnsupportedEncodingException e) {
            logger.error("导出excel失败", e);
        } catch (IOException e) {
            logger.error("导出excel失败", e);
        }
    }

    /**
     * @Description 根据模板导出excel
     * @Author kuiwang
     * @Date 10:41 2019/7/25
     * @param fileName  导出文件名称
     * @param excelTemplatePath     excel模板路径 **.xls
     * @param map   数据集合
     * @param request
     * @param response
     * @Return
     */
    protected void writeToExcelTemplate(String fileName, String excelTemplatePath, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateExportParams params = new TemplateExportParams(excelTemplatePath);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            if (isIE(request)) {
                fileName = URLEncoder.encode(fileName,ENCODING_UTF8);
            } else {
                fileName = new String(fileName.getBytes(ENCODING_UTF_8),ENCODING_ISO);
            }
            response.setHeader("content-disposition", "attachment;filename=" + fileName +EXPORT_SUFFIX_EXCEL);
            response.setContentType("application/octet-stream");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (UnsupportedEncodingException e) {
            logger.error("导出excel失败", e);
        } catch (IOException e) {
            logger.error("导出excel失败", e);
        }
    }

    /**
     * 根据模板导出excel
     * @param fileName  文件名称
     * @param excelTemplatePath  excel模板路径
     * @param map   传入的数据集合
     * @param columns  需要合并的字段个数
     * @param startRow 开始行
     * @param endRow 结束行
     * @param ints 合并的字段下标数组
     * @param request
     * @param response
     */
    protected void writeToExcelTemplateNeedMerge(String fileName, String excelTemplatePath, Map<String, Object> map,
                                                 Integer columns, int startRow, int endRow, int[] ints, HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateExportParams params = new TemplateExportParams(excelTemplatePath);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            Map<Integer, int[]> mergeMap = new HashMap<Integer, int[]>(16);
            for (int i = 0; i < columns; i++) {
                mergeMap.put(i, ints);
            }
            PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), mergeMap,startRow,endRow);
            if (isIE(request)) {
                fileName = URLEncoder.encode(fileName,ENCODING_UTF8);
            } else {
                fileName = new String(fileName.getBytes(ENCODING_UTF_8),ENCODING_ISO);
            }
            response.setHeader("content-disposition", "attachment;filename=" + fileName +EXPORT_SUFFIX_EXCEL);
            response.setContentType("application/octet-stream");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (UnsupportedEncodingException e) {
            logger.error("导出excel失败", e);
        } catch (IOException e) {
            logger.error("导出excel失败", e);
        }
    }

    /**
     *  根据word 模板导出word文件
     * @param fileName  导出文件的名称
     * @param wordTemplatePath  word模板文件路径 **.docx
     * @param map   word模板中数据
     * @param request
     * @param response
     */
    protected void writeToWordTemplate(String fileName, String wordTemplatePath, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        try {
            XWPFDocument doc = WordExportUtil.exportWord07(wordTemplatePath, map);
            // 直接下载不弹框
            if (isIE(request)) {
                fileName = URLEncoder.encode(fileName, ENCODING_UTF8);
            } else {
                fileName = new String(fileName.getBytes(ENCODING_UTF_8), ENCODING_ISO);
            }
            response.setHeader("content-disposition", "attachment;filename=" + fileName + EXPORT_SUFFIX_DOCX);
            response.setContentType("application/octet-stream");
            response.flushBuffer();
            doc.write(response.getOutputStream());
        } catch (Exception e) {
            logger.error("导出word失败", e);
        }
    }

    /**
     * @Description 导出pdf
     * @Author kuiwang
     * @Date 10:35 2019/7/25
     * @param title 标题
     * @param fileName  文件名称
     * @param pojoClass 导出实体类  **.class
     * @param dataSet   数据集合
     * @param request
     * @param response
     * @Return
     */
    protected void writeToPdf(String title, String fileName, Class<?> pojoClass, Collection<?> dataSet, HttpServletRequest request, HttpServletResponse response) {
        try {
            PdfExportParams params = new PdfExportParams(title, "system date:" + new Date());
            ByteArrayOutputStream baos = this.createTemporaryOutputStream();
            Document document = PdfExportUtil.exportPdf(params, pojoClass, dataSet, baos);
            if (isIE(request)) {
                fileName = URLEncoder.encode(fileName, ENCODING_UTF8);
            } else {
                fileName = new String(fileName.getBytes(ENCODING_UTF_8), ENCODING_ISO);
            }
            response.setHeader("content-disposition", "attachment;filename=" + fileName + EXPORT_SUFFIX_PDF);
            this.writeToResponse(response, baos);
        } catch (UnsupportedEncodingException e) {
            logger.error("导出pdf失败", e);
        } catch (IOException e) {
            logger.error("导出pdf失败", e);
        }
    }


    /**
     * @Description 根据模板导出pdf文件, 生成后直接导出下载
     *              参考：https://www.cnblogs.com/wangpeng00700/p/8418594.html
     * @Author kuiwang
     * @Date 14:26 2019/7/2
     * @param pdfTempLatePath   pdf模板路径
     * @param fileName  生成文件名称
     * @param map  传入pdf的数据
     * @param request
     * @param response
     * @Return
     */
    public void writeToPdfTemplate(String pdfTempLatePath, String fileName, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
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
     * @Description 根据word模板导出pdf文件
     *  主要没有找到破解的版的，就将第二步分解成2,3,4了，如果各位小伙伴找到破解的，欢迎留言，或者邮件 wangkui_wkgi@163.com 感谢！
     * @param fileName  导出文件的名称
     * @param wordTemplatePath  word模板文件路径**.docx
     * @param map   word模板中数据
     * @param request
     * @param response
     */
    protected void writeWordTemplateToPdf(String fileName, String wordTemplatePath, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        try {
            // 1.生成word文档
            long l = System.currentTimeMillis();
            XWPFDocument doc = WordExportUtil.exportWord07(wordTemplatePath, map);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            doc.write(bos);

            // 2.第一页添加空白页生成新的word文档（去水印使用）
            com.spire.doc.Document document = new com.spire.doc.Document();
            com.spire.doc.Document blankDoc = new com.spire.doc.Document();
            Section section = blankDoc.addSection();
            Paragraph paragraph = section.addParagraph();
            paragraph.appendText("添加空白页做去除水印");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            blankDoc.saveToFile(os, FileFormat.Docm_2010);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            document.loadFromStream(is, FileFormat.Docm_2010);
            os.close();
            is.close();
            blankDoc.close();
            // 合成一个word文档
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            document.insertTextFromStream(bis, FileFormat.Docm_2010);
            bos.close();
            bis.close();

            // 3.转换为pdf文档
            ByteArrayOutputStream pdfBos = new ByteArrayOutputStream();
            document.saveToFile(pdfBos, FileFormat.PDF);
            ByteArrayInputStream pdfBis = new ByteArrayInputStream(pdfBos.toByteArray());
            pdfBos.close();
            pdfBis.close();
            document.close();

            // 4.去除第一空白页并导出下载
            if (isIE(request)) {
                fileName = URLEncoder.encode(fileName, ENCODING_UTF8);
            } else {
                fileName = new String(fileName.getBytes(ENCODING_UTF_8), ENCODING_ISO);
            }
            response.setHeader("content-disposition", "attachment;filename=" + fileName + EXPORT_SUFFIX_PDF);
            response.setContentType("application/octet-stream");
            response.flushBuffer();
            PDDocument pdDocument = PDDocument.load(pdfBis);
            pdDocument.removePage(0);
            pdDocument.save(response.getOutputStream());
            pdDocument.close();
            logger.error("导出pdf完成,耗时：" + (System.currentTimeMillis() - l) + " 毫秒");
        } catch (Exception e) {
            logger.error("导出pdf失败", e);
        }
    }
}

