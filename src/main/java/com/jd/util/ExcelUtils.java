package com.jd.util;

import jxl.*;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.commons.beanutils.BeanUtils;

/**
 * excel导出工具，基于jxl 2.6
 *
 * @author zhenghaohao
 */
public class ExcelUtils {
    private ExcelUtils() {
    }

    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);


    private static String DEFAULT_FILE_NAME = "导出文件";
    private static String DEFAULT_SHEET_NAME = "sheet1";

    private static Integer SUCCESS = Integer.valueOf(1);
    private static Integer FAILED = Integer.valueOf(0);

    /**
     * 对外暴露的接口 导出excel
     *
     * @param request
     * @param response
     * @param rowList    数据列表
     * @param columNames 列名
     * @param filedNames 与列名对应的成员属性名
     * @param <T>
     */
    public static <T> void exportExcel(HttpServletRequest request, HttpServletResponse response,
                                       List<T> rowList, String[] columNames, String[] filedNames) {
        if (columNames == null || columNames.length <= 0)
            throw new IllegalArgumentException("列名不能为空");

        if (filedNames == null || filedNames.length <= 0)
            throw new IllegalArgumentException("必须设置相应的成员变量");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = DEFAULT_FILE_NAME + sdf.format(new Date());

        setResponseHeader(response, filename);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            generateExcel(request, response, rowList, columNames, filedNames, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取excel数据  每一行数据对应一个map
     *
     * @param is
     * @param fieldNames 与excel中列名一一对应的bean属性名，包括顺序
     * @return
     */
    public static List<Map<String, String>> getDataFromExcel(InputStream is, String[] fieldNames) {
        if (is == null)
            return Collections.emptyList();


        if (fieldNames == null || fieldNames.length <= 0)
            throw new IllegalArgumentException("必须给出属性名");

        Workbook workbook = null;

        try {
            //从输入流获得excel对象
            workbook = Workbook.getWorkbook(is);
            logger.debug(workbook);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        if (workbook == null)
            return Collections.emptyList();

        //获取工作表及数据行数
        Sheet sheet0 = workbook.getSheet(0);
        int rows = sheet0.getRows();
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>(rows - 1);
        //读取数据
        for (int i = 1; i < rows; i++) {
            Map<String, String> map = new HashMap<String, String>(fieldNames.length);
            for (int j = 0; j < fieldNames.length; j++) {
                Cell cell = sheet0.getCell(j, i);
                map.put(fieldNames[j], cell.getContents());
            }
            mapList.add(map);
        }
        return mapList;
    }


    public static <T> List<T> assembleBeans(List<Map<String, String>> mapList, Class<T> tClass, String[] fieldNames, CheckData checkData) {
        if (mapList == null || mapList.isEmpty() || tClass == null || checkData == null)
            return Collections.emptyList();

        List<T> beanList = new ArrayList<T>(mapList.size());
        for (Map<String, String> data : mapList) {
            //校验返回true，表示数据无异常
            if (!checkData.check(data)) {
                continue;
            }

            T entity = null;

            try {
                entity = tClass.newInstance();
                BeanUtils.populate(entity, data);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

    /*        for (String fieldName : fieldNames) {
                try {
                    //转换日期类型怎么办
                    BeanUtils.setProperty(entity, fieldName, data.get(fieldName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }*/
            beanList.add(entity);
        }
        return beanList;
    }

    /**
     * 生成excel的方法
     *
     * @param request
     * @param response
     * @param rowList
     * @param columNames
     * @param filedNames
     * @param outputStream
     * @param <T>
     */
    private static <T> void generateExcel(HttpServletRequest request, HttpServletResponse
            response, List<T> rowList, String[] columNames, String[] filedNames, OutputStream outputStream) {

        WritableWorkbook workbook = null;
        // 全局设置
        WorkbookSettings setting = new WorkbookSettings();
        java.util.Locale locale = new java.util.Locale("zh", "CN");
        setting.setLocale(locale);
        setting.setEncoding("UTF-8");
        try {
            //创建工作薄
            workbook = Workbook.createWorkbook(outputStream);
            //创建sheet
            WritableSheet sheet = workbook.createSheet(DEFAULT_SHEET_NAME, 0);
            SheetSettings settings = sheet.getSettings();
            settings.setHorizontalFreeze(1);//冻结表头

            //设置标题
            setColumHeader(sheet, columNames);

            //填充单元格内容
            addContext(sheet, rowList, filedNames);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //写入文件
            try {
                workbook.write();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 添加标题内容并设置样式
     *
     * @param sheet      表
     * @param columNames 输入的列名数组
     */
    private static void setColumHeader(WritableSheet sheet, String[] columNames) {
        //字体
        WritableFont writableFont = new WritableFont(WritableFont.ARIAL, WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD);
        //格式
        WritableCellFormat format = new WritableCellFormat(writableFont);
        try {
            format.setWrap(true);//自动换行
            format.setAlignment(Alignment.CENTRE);
            format.setVerticalAlignment(VerticalAlignment.CENTRE);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        Label label = null;
        int colSize = columNames.length;

        String colName = null;
        for (int i = 0; i < colSize; i++) {
            colName = columNames[i];
            if (colName == null || colName.equals(""))
                colName = "";

            //填充标题栏中的单元格
            label = new Label(i, 0, colName, format);
            try {
                sheet.addCell(label);
            } catch (WriteException e) {
                e.printStackTrace();
            }
            //默认列宽
//            sheet.setColumnView(i, 20);
        }
    }


    /**
     * 填充每个单元格内容
     *
     * @param sheet      当前的表
     * @param list       待填充的数据
     * @param fieldNames 相应的bean属性名（和列名一一对应）
     * @param <T>
     */
    private static <T> void addContext(WritableSheet sheet, List<T> list, String[] fieldNames) {
        int rows = list.size();
        Label label = null;
//        //字体
//        WritableFont writableFont = new WritableFont(WritableFont.ARIAL, WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD);
//        //格式
//        WritableCellFormat format = new WritableCellFormat(writableFont);
        int cols = fieldNames.length;

        Object value = null;

        for (int i = 0; i < rows; i++) {
            T t = (T) list.get(i);
            for (int j = 0; j < cols; j++) {
                value = PropertyUtils.getProperty(t, fieldNames[j]) == null ? "" : PropertyUtils.getProperty(t, fieldNames[j]);
                label = new Label(j, (i + 1), value + "");
                try {
                    sheet.addCell(label);
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用response向前台输出excel,设置相应的响应头
     * 自动出现下载
     *
     * @param response
     * @param name
     */
    public static void setResponseHeader(HttpServletResponse response, String name) {
        response.setContentType("application/msexcel;charset=UTF-8");
        name = name + ".xls";
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
    }


}
