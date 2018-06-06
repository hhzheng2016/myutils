package com.jd.util;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * excel导出工具，基于jxl 2.6
 *
 * @author zhenghaohao
 */
public class ExcelUtils {
    private ExcelUtils() {
    }

    private static String DEFAULT_FILE_NAME = "导出文件";
    private static String DEFAULT_SHEET_NAME = "sheet1";

    /**
     * 对外暴露的接口
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
     * @param sheet 当前的表
     * @param list  待填充的数据
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
