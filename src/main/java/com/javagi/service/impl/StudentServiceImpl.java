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
        Student s1 = new Student("1","1","1","1","1","1");
        Student s2 = new Student("2","2","2","2","2","2");
        students.add(s1);
        students.add(s2);
        return students;
    }



}
