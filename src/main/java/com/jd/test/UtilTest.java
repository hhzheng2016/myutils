package com.jd.test;

import com.jd.domain.Student;
import com.jd.util.CheckData;
import com.jd.util.ExcelUtils;
import com.jd.util.PropertyUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UtilTest {

    @Test
    public void test2() {
        Student s2 = new Student();
        s2.setName("李四");
        s2.setBirthday(new Date());
        s2.setId(1);

        System.out.println(s2);

        System.out.println(PropertyUtils.getProperty(s2, "name"));
        System.out.println(PropertyUtils.getProperty(s2, "birthday"));
        System.out.println(PropertyUtils.getProperty(s2, "id"));
    }

    @Test
    public void assembleBeanTest() {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 3; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", i + "");
            map.put("name", "name" + i);
            map.put("birthday", new Date().toString());
            map.put("major", "major" + i);

            mapList.add(map);
        }
        String[] fileds = {"id", "name", "birthday", "major"};
        List<Student> list;
        ConvertUtils.register(new Converter() {
            @Override
            public Date convert(Class type, Object value) {
                Date date=null;
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date=sdf.parse((String)value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date;
            }
        }, Date.class);
        list = ExcelUtils.assembleBeans(mapList, Student.class, fileds, new CheckData() {
            public boolean check(Map<String, String> data) {
                return (!data.get("id").isEmpty() || !data.get("name").isEmpty() || !data.get("birthday").isEmpty()
                        || !data.get("major").isEmpty());
            }
        });

        for (Student student : list) {
            System.out.println(student);
        }
    }
}
