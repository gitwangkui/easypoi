package com.javagi.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.javagi.utils.ExportUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.css.parser.property.PrimitivePropertyBuilders;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 大数据excel 表格导出
 * @Author kuiwang
 * @Date 2019/7/1 15:27
 * @Version 1.0
 */
@RestController
public class SysUser2Controller extends ExportUtil {



    //*************************************************************************
/*    @GetMapping(value = "/exportAllSalesRecordSellIn")
    public void exportAllSalesRecordSellIn(HttpServletResponse response) throws Exception {
        Long t1 = System.currentTimeMillis();
        System.out.println();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("page","1");paramsMap.put("limit","20000");
        ExportParams params = new ExportParams("销售数据统计", "销售数据");
        Workbook workbook = null;
        PageData<Mid_salesRecordSellIn> allSalesRecordSellIn = midSalesRecordSellInService.getAllSalesRecordSellIn(paramsMap);
        int totalPage = (int)Math.ceil((double)allSalesRecordSellIn.getTotal()/20000);
        for(int currentPage = 1; currentPage <= totalPage; currentPage++){
            paramsMap.put("page",String.valueOf(currentPage));
            Long l1 = System.currentTimeMillis();
            PageData<Mid_salesRecordSellIn> mr = midSalesRecordSellInService.getAllSalesRecordSellIn(paramsMap);
            Long l2 = System.currentTimeMillis();
            System.out.println("查询"+currentPage+"耗时:"+(l2-l1)/1000+"秒");
            workbook = ExcelExportUtil.exportBigExcel(params, Mid_salesRecordSellIn.class, mr.getList());
        }
        ExcelExportUtil.closeExportBigExcel();

        String fileName = "saleData.xlsx";
        //告诉浏览器下载excel
        downloadExcel(fileName, workbook, response);
        Long t2 = System.currentTimeMillis();
        System.out.println("总共耗时:"+(t2 - t1)/1000+"秒");
    }

    protected void downloadExcel(String filename, Workbook workbook, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }*/


}
