package util;

import model.Sheet;
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

    public static void setEntryXY(Sheet sheet) throws Exception {
        int physicalRows = ExcelUtil.countRows(sheet.getXssfSheet());
        int pysicalColumns = ExcelUtil.countCells(sheet.getXssfSheet().getRow(0));

        logger.debug(
                String.format("(physicalX, physicalY) = (%d, %d)", pysicalColumns, physicalRows));

        for (int rIdx = 0; rIdx < physicalRows; rIdx++) {
            XSSFRow row = ExcelUtil.getRow(sheet.getXssfSheet(), rIdx);
            if (row != null) {
                for (int cIdx = 0; cIdx < pysicalColumns; cIdx++) {
                    XSSFCell cell = ExcelUtil.getCell(row, cIdx);
                    if (cell != null) {
                        String value = ExcelUtil.getCellValue(cell);
                        // === Y 측정 ===
                        if(cIdx == 0) {
                            if (value.trim().equals("")) {
                                sheet.setLengthY(rIdx);
                            }
                        } else {
                            if(sheet.getLengthX() != Sheet.INIT_X) {
                                break;
                            }
                        }
                        // === X 측정 ===
                        if(sheet.getLengthX() == Sheet.INIT_X) {
                            if (value.trim().equals("")) {
                                sheet.setLengthX(cIdx);
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
        if(sheet.getLengthX() == Sheet.INIT_X) {
            sheet.setLengthX(pysicalColumns);
        }
        if(sheet.getLengthY() == Sheet.INIT_Y) {
            sheet.setLengthY(physicalRows);
        }

        logger.debug(String.format("(entryX, entryY) = (%d, %d)", sheet.getLengthX(), sheet.getLengthY()));

        if(sheet.getLengthX() < 2 || sheet.getLengthY() <2)
            throw new Exception();
    }

}
