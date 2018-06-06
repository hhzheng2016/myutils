package com.jd.controller.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StrToDateConverter implements Converter<String,Date> {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    public Date convert(String s) {
        try {
            Date date=sdf.parse(s);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
