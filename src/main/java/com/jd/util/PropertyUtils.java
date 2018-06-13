package com.jd.util;

import java.lang.reflect.Field;


public class PropertyUtils {

    /**
     * 获取实例对象对应属性名的值
     *
     * @param t         对象实例
     * @param fieldName 属性名
     * @param <T>
     * @return 属性值
     */
    public static <T> String getProperty(T t, String fieldName) {
        Field[] fields = t.getClass().getDeclaredFields();
        String result = null;
        for (Field field : fields) {
            //类中的成员变量是私有的,必须进行此步
            field.setAccessible(true);
            if (field.getName().equals(fieldName)) {
                try {
                    result = field.get(t).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /*public static <T> void setProperty(T t, String fieldName, String s) {
        Field field = null;
        try {
            field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Class c=field.getType();

            field.set(fieldName, s);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }*/



}
