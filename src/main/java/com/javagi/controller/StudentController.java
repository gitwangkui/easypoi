package com.javagi.controller;

import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.javagi.model.Student;
import com.javagi.service.StudentService;
import com.javagi.utils.ExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author kuiwang
 * @Date 2019/7/1 15:27
 * @Version 1.0
 */
@RequestMapping("/export")
@RestController
public class StudentController extends ExportUtil {

    private static final String EXPORT_USER_TEST = "测试用户报表";
    private static final String EXPORT_USER_TEST_MODEL = "doc/测试用户报表导出模板.xls";
    private static final String EXPORT_USER_TEST_WORD = "doc/测试导出word文档.docx";

    @Autowired
    private StudentService studentService;

    /**
     * @Description  普通导出excel
     * @Author kuiwang
     * @Date 17:57 2019/7/1
     * @param request
     * @param response
     * @Return
     */
    @GetMapping("/exportExcel")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) {
        List<Student> students = studentService.getAllStudents();
        this.writeToExcel("学生信息", "学生详情信息", Student.class, students, request, response);
        return "Success!";
    }

    /**
     * @Description  根据excel模板导出表格 ok
     * @Author kuiwang
     * @Date 17:57 2019/7/1
     * @param request
     * @param response
     * @Return
     */
    @GetMapping("/exportAll")
    public String exportExcelAll(HttpServletRequest request, HttpServletResponse response) {
        List<Student> students = studentService.getAllStudents();
        Map<String, Object> map = new HashMap<>();
        map.put("students", students);
        this.writeToExcelTemplate(EXPORT_USER_TEST, EXPORT_USER_TEST_MODEL, map, request, response);
        return "Success!";
    }

    /**
     * @Description  根据excel模板导出合并表格 ok
     * @Author kuiwang
     * @Date 17:57 2019/7/1
     * @param request
     * @param response
     * @Return
     */
    @GetMapping("/writeToExcelTemplateNeedMerge")
    public String writeToExcelTemplateNeedMerge(HttpServletRequest request, HttpServletResponse response) {
        List<Student> students = studentService.getAllStudents();
        Map<String, Object> map = new HashMap<>();
        map.put("students", students);
        this.writeToExcelTemplateNeedMerge(EXPORT_USER_TEST, EXPORT_USER_TEST_MODEL, map,
                3, 2, 2+students.size(), new int[]{1,2}, request, response);
        return "Success!";
    }


    /**
     * @Description 普通导出pdf ok
     * @Author kuiwang
     * @Date 18:01 2019/7/1
     * @param request
     * @param response
     * @Return
     */
    @GetMapping("/exportPdf")
    public String exportPdf(HttpServletRequest request, HttpServletResponse response) {
        List<Student> students = studentService.getAllStudents();
        this.writeToPdf("学生信息", "学生详情信息", Student.class, students, request, response);
        return "Success!";
    }

    /**
     * @Description 根据模板导出pdf ok
     * @Author kuiwang
     * @Date 10:43 2019/7/2
     * @param request
     * @param response
     * @Return
     */
    @GetMapping("/exportPdfTemplate")
    public String exportPdfTemplate(HttpServletRequest request, HttpServletResponse response) {
        // 文字
        Map<String,String> map = new HashMap<>();
        map.put("id","王谋仁");
        map.put("one","2018年1月1日");
        map.put("two","晴朗");
        map.put("zq_xm","打羽毛球");
        map.put("cm_xm","打羽毛球");
        map.put("tj_xm","打羽毛球");

        // 图片
        Map<String,String> map2 =new HashMap<>();
        map2.put("img","c:/50336.jpg");

        // 调用
        Map<String,Object> o=new HashMap();
        o.put("charMap",map);
        o.put("imgMap",map2);

        String pdfTempLatePath = "doc/要导出pdf模板.pdf";
        this.writeToPdfTemplate(pdfTempLatePath, "导出后新pdf名称", o, request, response);
        return "Success!";
    }


    /**
     * @Description  根据模板导出word
     * @Author kuiwang
     * @Date 11:43 2019/7/25
     * @param request
     * @param response
     * @Return
     */
    @GetMapping("/exprotWordTemplate")
    public String exprotWordTemplate(HttpServletRequest request, HttpServletResponse response){
        long l = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
        map.put("department", "Easypoi");
        map.put("person", "JueYue");
        map.put("time", sdf.format(new Date()));
        map.put("me","JueYue");
        map.put("date", "2015-01-03");
        this.writeToWordTemplate("要导出word的名称", EXPORT_USER_TEST_WORD, map, request, response);
        return "导出word 成功，耗时:" + (System.currentTimeMillis() - l) + " 毫秒";
    }

    /**
     * @Description  根据word模板导出pdf
     * @Author kuiwang
     * @Date 11:43 2019/7/25
     * @param request
     * @param response
     * @Return
     */
    @GetMapping("/writeWordTemplateToPdf")
    public String writeWordTemplateToPdf(HttpServletRequest request, HttpServletResponse response){
        long l = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat();
        map.put("department", "Easypoi");
        map.put("person", "JueYue");
        map.put("time", sdf.format(new Date()));
        map.put("me","JueYue");
        map.put("date", "2015-01-03");
        this.writeWordTemplateToPdf("要导出word的名称", EXPORT_USER_TEST_WORD, map, request, response);
        return "导出pdf 成功，耗时:" + (System.currentTimeMillis() - l) + " 毫秒";
    }
}
