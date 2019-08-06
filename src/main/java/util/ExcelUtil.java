package util;

import model.Multilingual;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static XSSFSheet getSheet(XSSFWorkbook book, int index) {
        return book.getSheetAt(0);
    }

    public static XSSFRow getRow(XSSFSheet sheet, int index) {
        return sheet.getRow(index);
    }

    public static XSSFCell getCell(XSSFRow row, int index) {
        return row.getCell(index);
    }

    public static int countSheets(XSSFSheet sheet) {
        return 0;
    }

    public static int countRows(XSSFSheet sheet) {
        return sheet.getPhysicalNumberOfRows();
    }

    public static int countCells(XSSFRow row) {
        return row.getPhysicalNumberOfCells();
    }

    public static String getCellValue(XSSFCell cell) {
        String result = "";
        switch (cell.getCellType()) {
            case FORMULA:
                result = cell.getCellFormula();
                break;
            case NUMERIC:
                result = cell.getNumericCellValue() + "";
                break;
            case STRING:
                result = cell.getStringCellValue() + "";
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue() + "";
                break;
            case ERROR:
                result = cell.getErrorCellValue() + "";
                break;
        }
        return result;
    }

    public static int[] getLengthXY(XSSFSheet sheet) throws Exception {
        int resultX = Multilingual.INIT_X;
        int resultY = Multilingual.INIT_Y;
        int physicalRows = ExcelUtil.countRows(sheet);
        int pysicalColumns = ExcelUtil.countCells(sheet.getRow(0));

        logger.debug(
                String.format("(physicalX, physicalY) = (%d, %d)", pysicalColumns, physicalRows));

        for (int rIdx = 0; rIdx < physicalRows; rIdx++) {
            XSSFRow row = ExcelUtil.getRow(sheet, rIdx);
            if (row != null) {
                for (int cIdx = 0; cIdx < pysicalColumns; cIdx++) {
                    XSSFCell cell = ExcelUtil.getCell(row, cIdx);
                    if (cell != null) {
                        String value = ExcelUtil.getCellValue(cell);
                        // === Y 측정 ===
                        if(cIdx == 0) {
                            if (value.trim().equals("")) {
                                resultY = rIdx;
                            }
                        } else {
                            if(resultX != Multilingual.INIT_X) {
                                break;
                            }
                        }
                        // === X 측정 ===
                        if(resultX == Multilingual.INIT_X) {
                            if (value.trim().equals("")) {
                                resultX = cIdx;
                            }
                        }
                    } else {
                        /*logger.debug("cell null");*/
                    }
                }
            } else {
                /*logger.debug("row null");*/
            }
        }
        if(resultX == Multilingual.INIT_X) {
            resultX = pysicalColumns;
        }
        if(resultY == Multilingual.INIT_Y) {
            resultY = physicalRows;
        }

        logger.debug(String.format("(entryX, entryY) = (%d, %d)", resultX, resultY));

        if(resultX < 2 || resultY <2)
            throw new Exception();
        
        return new int[] { resultX, resultY };
    }



}
