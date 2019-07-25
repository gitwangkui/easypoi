package com.javagi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.javagi.model.Student;
import com.javagi.service.StudentService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xwpf.usermodel.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author kuiwang
 * @Date 2019/7/1 15:35
 * @Version 1.0
 */
@Service
public class StudentServiceImpl implements StudentService {

    private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("1","张三","男","123@qq.com","wx123","176123"));
        students.add(new Student("2","张三","男","456@qq.com","wx456","176456"));
        students.add(new Student("3","李莫愁","女","1001@qq.com","wx1001","1761001"));
        students.add(new Student("4","李莫愁","女","1002@qq.com","wx1002","1761002"));
        students.add(new Student("5","旭宝宝+PDD+卢本伟+马老师","男","0001@qq.com","wx0001","1008611"));
        return students;
    }




}
