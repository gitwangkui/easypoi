package com.javagi.service;


import com.javagi.model.Student;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StudentService {


    List<Student> getAllStudents();

}
