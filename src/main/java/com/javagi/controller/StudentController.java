package com.javagi.controller;

import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.javagi.model.Student;
import com.javagi.service.StudentService;
import com.javagi.utils.ExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author kuiwang
 * @Date 2019/7/1 15:27
 * @Version 1.0
 */
@RestController
public class StudentController extends ExportUtil {

    private static final String EXPORT_USER_TEST = "测试用户报表";
    private static final String EXPORT_USER_TEST_MODEL = "doc/测试用户报表导出模板.xls";

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
     * @Description  根据excel模板导出表格
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
        TemplateExportParams params = new TemplateExportParams(EXPORT_USER_TEST_MODEL);
        this.writeToExcelTemplate(EXPORT_USER_TEST, params, map, request, response);
        return "Success!";
    }

    /**
     * @Description 普通导出pdf
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
     * @Description 根据模板导出pdf
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

        String pdfTempLatePath = "doc/wordToPdf.pdf";
        this.writeToPdfTemplate(pdfTempLatePath, "新pdf名称", o, request, response);
        return "Success!";
    }

}
