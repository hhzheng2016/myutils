package com.jd.domain;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Student {
    private Integer id;
    private String name;
    private Date birthday;
    private String major;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", major='" + major + '\'' +
                '}';
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer stuid) {
        this.id = stuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
            this.birthday = birthday;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
