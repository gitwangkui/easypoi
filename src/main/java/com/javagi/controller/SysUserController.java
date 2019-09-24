package com.javagi.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.javagi.utils.ExportUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.css.parser.property.PrimitivePropertyBuilders;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Description
 * @Author kuiwang
 * @Date 2019/7/1 15:27
 * @Version 1.0
 */
@RestController
public class SysUserController extends ExportUtil {

   /* @RequestMapping("/export")
    public void export(HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        Workbook workbook = bigExcel(1, params, null, new ExportParams("海贼王", "海贼王"), new PrimitivePropertyBuilders.Page<>());
        ExcelExportUtil.closeExportBigExcel();
        downLoadExcel("海贼王.xls", response, workbook);
    }


    private Workbook bigExcel(int pageNum, Map<String, Object> params, Workbook workbook, ExportParams exportParams, Page<SysUser> page) {
        //分页查询数据
        page.setCurrent(pageNum);
        page.setSize(1000);
        page.setCondition(params);
        page = this.getData(sysUserService.queryPage(page));
        List<SysUser> users = FastJsonUtils.toList(FastJsonUtils.toJSONString(page.getRecords()), SysUser.class);

        workbook = ExcelExportUtil.exportBigExcel(exportParams, SysUser.class, users);

        //如果不是最后一页，递归查询
        if (page.getPages() > pageNum) {
            bigExcel(pageNum + 1, params, workbook, exportParams, page);
        }
        return workbook;
    }

    private void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("content-Type", "application/vnd.ms-excel");
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            System.out.println("=========异常"+ e.getMessage());
        }
    }*/

}
