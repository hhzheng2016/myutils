package com.jd.service.impl;

import com.jd.dao.StudentMapper;
import com.jd.domain.Student;
import com.jd.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper mapper;

    public Student findStudentById(Integer id) {
        return mapper.findStudentById(id);
    }

    public void updateStudent(Student student) {
        mapper.updateStudent(student);
    }

    public void deleteStudentById(Integer id) {
        mapper.deleteStudentById(id);
    }


    public void addStudent(Student student) {
        mapper.addStudent(student);
    }

    public List<Student> findAll() {
        return mapper.findAll();
    }
}
