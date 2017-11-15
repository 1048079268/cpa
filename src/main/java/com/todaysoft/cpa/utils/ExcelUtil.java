package com.todaysoft.cpa.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/15 13:35
 */
public class ExcelUtil {
    public static void createByList(List<List<String>> lists,String path) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        for (int i=0;i<lists.size();i++){
            List<String> list=lists.get(i);
            Row row = sheet.createRow(i);
            for (int j=0;j<list.size();j++){
                row.createCell(j).setCellValue(list.get(j));
            }
        }
        FileOutputStream fos = new FileOutputStream(path);
        wb.write(fos);
        fos.close();
    }
}
