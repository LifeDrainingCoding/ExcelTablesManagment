package com.kursach.ckursach;

import android.util.Log;
import androidx.core.content.UnusedAppRestrictionsBackportCallback;
import androidx.resourceinspection.annotation.Attribute;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
public class CSVconverter {
    private static final String TAG = "CSVconverter";
    public String convertCSVtoExcel(String csvPath  ){

        String filename = FilenameUtils.getBaseName(csvPath);
        try {
            // Путь к CSV файлу

            // Путь к Excel файлу
            String excelFile = filename+".xlsx";

            // Создание Workbook
            Workbook workbook = new XSSFWorkbook(); // XSSF для Excel 2007 и выше

            // Чтение данных из CSV файла
            InputStream inputStream = Files.newInputStream(Paths.get(csvPath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine(); // Чтение первой строки (заголовки)
            int lineNumber = 0;
           Sheet sheet  = workbook.createSheet("Sheet1");
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(",");
                if (lineNumber == 0) {
                    // Создание Sheet
                    // Создание Row для заголовков
                    Row headerRow = sheet.createRow(lineNumber);
                    for (int i = 0; i < cells.length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(cells[i]);
                    }
                } else {
                    Row dataRow = sheet.createRow(lineNumber);
                    for (int i = 0; i < cells.length; i++) {
                        Cell cell = dataRow.createCell(i);
                        cell.setCellValue(cells[i]);
                    }
                }
                lineNumber++;
            }
            reader.close();

            // Сохранение Excel файла
            OutputStream outputStream = Files.newOutputStream(Paths.get(excelFile));
            workbook.write(outputStream);
            outputStream.close();

            Log.i(TAG, "convertCSVtoExcel: Successfully converted to excel file ");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
