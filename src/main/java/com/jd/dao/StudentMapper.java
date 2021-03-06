package com.jd.dao;

import com.jd.domain.Student;

import java.util.List;

public interface StudentMapper {
     Student findStudentById(Integer id);
     void updateStudent(Student student);
     void deleteStudentById(Integer id);
     void addStudent(Student student);
     List<Student> findAll();
}
