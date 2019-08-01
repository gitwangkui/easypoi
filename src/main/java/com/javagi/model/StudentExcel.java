package com.javagi.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class StudentExcel implements Serializable {

    @Excel(name = "编号" )
    private String id;
    @Excel(name = "姓名" )
    private String one;
    @Excel(name = "姓别" )
    private String two;
    @Excel(name = "电子邮件" )
    private String zq_xm;
    @Excel(name = "微信号" )
    private String cw_xm;
    @Excel(name = "联系电话" )
    private String tj_xm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getZq_xm() {
        return zq_xm;
    }

    public void setZq_xm(String zq_xm) {
        this.zq_xm = zq_xm;
    }

    public String getCw_xm() {
        return cw_xm;
    }

    public void setCw_xm(String cw_xm) {
        this.cw_xm = cw_xm;
    }

    public String getTj_xm() {
        return tj_xm;
    }

    public void setTj_xm(String tj_xm) {
        this.tj_xm = tj_xm;
    }

}
