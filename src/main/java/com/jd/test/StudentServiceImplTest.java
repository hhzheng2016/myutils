package com.jd.test;


import com.jd.domain.Student;
import com.jd.service.StudentService;
import com.jd.service.impl.StudentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StudentServiceImplTest {
    @Resource
    StudentService studentService;

    @Test
    public void testFindAll() {
        List<Student> students = studentService.findAll();
        for (Student student : students) {
            System.out.println(student);
        }
    }

    @Test
    public void testSelect() {
        Student student = studentService.findStudentById(1);
        System.out.println(student);
    }

    @Test
    public void testInsert() {
        Date date = new Date();
        Student student = new Student();
        student.setName("testname");
        student.setBirthday(date);
        student.setMajor("数学4");
        studentService.addStudent(student);
    }


    @Test
    public void testUpdate() {
        Student student = new Student();
        student.setId(5);
        student.setName("testname");
        student.setBirthday(new Date());
        student.setMajor("数学5");
        studentService.updateStudent(student);
    }

    @Test
    public void testDelete(){
        int id=5;
        studentService.deleteStudentById(id);
    }

}
