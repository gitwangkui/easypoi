package com.javagi.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description
 * @Author kuiwang
 * @Date 2019/9/9 11:28
 * @Version 1.0
 */
public class MsgClient {

    @Excel(name = "出生日期", format = "yyyy-MM-dd")
    private Date birthday;

    @Excel(name = "姓名")
    private String clientName;

    @Excel(name = "电话")
    private String clientPhone;

    @Excel(name = "创建人")
    private String createBy;

    @Excel(name = "主键id")
    private String id;

    @Excel(name = "备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}