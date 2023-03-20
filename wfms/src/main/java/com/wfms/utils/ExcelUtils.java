package com.wfms.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class ExcelUtils {
    public void createCell(Workbook workbook, Sheet sheet, Row row, String cellValue, int index) {
        Font fNotBold = sheet.getWorkbook().createFont();
        fNotBold.setFontName("Arial");
        fNotBold.setFontHeightInPoints((short) 11);

        fNotBold.setBold(false);

        final CellStyle cellStyleCommon = workbook.createCellStyle();
        cellStyleCommon.setWrapText(true);
        cellStyleCommon.setFont(fNotBold);
        cellStyleCommon.setBorderTop(BorderStyle.THIN);
        cellStyleCommon.setBorderBottom(BorderStyle.THIN);
        cellStyleCommon.setBorderLeft(BorderStyle.THIN);
        cellStyleCommon.setBorderRight(BorderStyle.THIN);

        Cell tempCell = row.createCell(index);
        tempCell.setCellValue(cellValue);
        tempCell.setCellStyle(cellStyleCommon);
    }
    public void createCellPercent(Workbook workbook, Sheet sheet, Row row, double cellValue, int index) {
        Font fNotBold = sheet.getWorkbook().createFont();
        fNotBold.setFontName("Arial");
        fNotBold.setFontHeightInPoints((short) 11);

        fNotBold.setBold(false);

        final CellStyle cellStyleCommon = workbook.createCellStyle();
        cellStyleCommon.setWrapText(true);
        cellStyleCommon.setFont(fNotBold);
        cellStyleCommon.setBorderTop(BorderStyle.THIN);
        cellStyleCommon.setBorderBottom(BorderStyle.THIN);
        cellStyleCommon.setBorderLeft(BorderStyle.THIN);
        cellStyleCommon.setBorderRight(BorderStyle.THIN);
        cellStyleCommon.setDataFormat(workbook.createDataFormat().getFormat("0.000%"));
        Cell tempCell = row.createCell(index);
        tempCell.setCellValue(cellValue);
        tempCell.setCellStyle(cellStyleCommon);
    }


    public ByteArrayResource getByteArrayResource(XSSFWorkbook wb) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            wb.write(byteArrayOutputStream);
        } finally {
            byteArrayOutputStream.close();
        }
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    public CellStyle getCellStyle(XSSFWorkbook wb, XSSFSheet sheet1) {
        final CellStyle cellStyleCommon = wb.createCellStyle();
        cellStyleCommon.setWrapText(true);
        cellStyleCommon.setBorderTop(BorderStyle.THIN);
        cellStyleCommon.setBorderBottom(BorderStyle.THIN);
        cellStyleCommon.setBorderLeft(BorderStyle.THIN);
        cellStyleCommon.setBorderRight(BorderStyle.THIN);
        Font f = sheet1.getWorkbook().createFont();
        f.setFontName("Arial");
        f.setFontHeightInPoints((short) 11);
        f.setBold(true);
        cellStyleCommon.setFont(f);
        return cellStyleCommon;
    }
    public static Sheet getSheet(byte[] fileContent, int sheetNum)
            throws IOException, InvalidFormatException {
        Workbook workbook = null;
        try (InputStream fContent = new ByteArrayInputStream(fileContent)) {
            workbook = WorkbookFactory.create(fContent);
            return workbook.getSheetAt(sheetNum);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if(workbook != null){
                workbook.close();
            }
        }
    }
    public static List<String> getHeaderSheet(Sheet sheet) {
        Row row = sheet.getRow(0);
        Iterator<Cell> cellIterator = row.cellIterator();

        List<String> listHeader = new ArrayList<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            listHeader.add(cell.getStringCellValue());
        }

        return listHeader;
    }
    public static List<String> getRowAt(byte[] content, int rowNum) throws IOException, InvalidFormatException {
        Sheet sheet = getSheet(content, 0);
        List<String> listCell = new ArrayList<>();
        if(sheet != null) {
            Row row = sheet.getRow(rowNum);
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    listCell.add(cell.getStringCellValue());
                } else {
                    listCell.add(null);
                }

            }
        }
        return listCell;
    }
}
