package com.javagi.model;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * @Description
 * @Author kuiwang
 * @Date 2019/7/8 14:25
 * @Version 1.0
 */
public class ExcelDataVO extends ImportParams {

    private String name;

    private Integer age;

    private String location;

    private String job;

    public ExcelDataVO() {
    }

    public ExcelDataVO(String name, Integer age, String location, String job) {
        this.name = name;
        this.age = age;
        this.location = location;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "ExcelDataVO{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", location='" + location + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}