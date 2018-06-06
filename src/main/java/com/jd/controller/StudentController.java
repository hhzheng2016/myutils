package com.jd.controller;

import com.jd.domain.Student;
import com.jd.service.StudentService;
import com.jd.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 查询所有用户
     */
    @RequestMapping("/findAll")
    public ModelAndView findStudents() {
        ModelAndView mv = new ModelAndView();
        List<Student> studentList = studentService.findAll();
        mv.addObject("studentList", studentList);
        mv.setViewName("findAll");
        return mv;
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, Model model) {
        String idStr = request.getParameter("id");
        Student student = studentService.findStudentById(Integer.parseInt(idStr));
        //Model底层使用的的就是request
        model.addAttribute("student", student);

        return "edit_student";
    }


    @RequestMapping("/update_student")
    public String update(Student student){
        studentService.updateStudent(student);
        return "success";
    }




    @RequestMapping("/register")
    public String register( ) {
        return "register";
    }

    @RequestMapping("add_student")
    public String addStudent(Student student){
        studentService.addStudent(student);
        return "success";
    }


    @RequestMapping("/delete")
    public String delete(int id){
        studentService.deleteStudentById(id);
        return "success";
    }


    @RequestMapping("/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        List<Student> studentList = studentService.findAll();
        String[] columNames={"姓名","生日","专业"};
        String[] filedNames={"name","birthday","major"};
        ExcelUtils.exportExcel(request, response, studentList, columNames, filedNames);
    }
}
