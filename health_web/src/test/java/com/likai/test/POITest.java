package com.likai.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {

    @Test
    public void fun01() throws IOException {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("E:\\hello.xlsx");
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheet) {
            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获得单元格中的值
                String value = cell.getStringCellValue();

                System.out.println(value);
            }
        }
        workbook.close();
    }

    @Test
    public void fun02() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("E:\\hello.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        try {
            for (int i = 0; i <= lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                short lastCellNum = row.getLastCellNum();
                for (short j = 0; j < lastCellNum; j++) {
                    String value = row.getCell(j).getStringCellValue();
                    System.out.println(value);
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

        workbook.close();
    }

    @Test
    public void fun03() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("李凯");
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("编号");
        row0.createCell(1).setCellValue("名称");
        row0.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("20");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小明");
        row2.createCell(2).setCellValue("20");

        FileOutputStream out = new FileOutputStream("E:\\likai.xlsx");
        workbook.write(out);
        out.close();
        workbook.close();
    }
}
