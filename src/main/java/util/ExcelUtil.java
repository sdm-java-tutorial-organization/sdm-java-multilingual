package util;

import model.Document;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static final int FIRST_ROW = 0;
    public static final int FIRST_COLUMN = 0;

    public static XSSFWorkbook initBook() {
        return new XSSFWorkbook();
    }

    public static XSSFSheet initSheet(XSSFWorkbook book, String name) {
        return book.createSheet(name);
    }

    public static XSSFRow initRow(XSSFSheet sheet, int index) {
        return sheet.createRow(index);
    }

    public static void initCells(XSSFRow row, List<String> values) {
        if(values != null && values.size()>1) {
            for(int i=0; i<values.size(); i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(values.get(i));
            }
        }
    }

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
        int resultX = Document.INIT_X;
        int resultY = Document.INIT_Y;
        int physicalRows = ExcelUtil.countRows(sheet);
        int pysicalColumns = ExcelUtil.countCells(sheet.getRow(0));

        logger.debug(
                String.format("(physicalX, physicalY) = (%d, %d)", pysicalColumns, physicalRows));

        for (int row = 0; row < physicalRows; row++) {
            XSSFRow line = ExcelUtil.getRow(sheet, row);
            if (line != null) {
                for (int column = 0; column < pysicalColumns; column++) {
                    XSSFCell cell = ExcelUtil.getCell(line, column);
                    if (cell != null) {
                        String value = ExcelUtil.getCellValue(cell);
                        // === Y 측정 ===
                        if(column == FIRST_COLUMN) {
                            if (value.trim().equals("")) {
                                resultY = row;
                            }
                        } else {
                            if(row != FIRST_ROW) {
                                break;
                            }
                        }

                        // === X 측정 ===
                        if(row == FIRST_ROW) {
                            if(resultX == Document.INIT_X) {
                                if (value.trim().equals("")) {
                                    resultX = column;
                                }
                            }
                        }
                    } else {
                        logger.debug("cell null : column " + column);
                        resultX = column;
                        break;
                    }
                }
            } else {
                logger.debug("row null : row " + row);
                resultY = row;
                break;
            }
        }
        if(resultX == Document.INIT_X) {
            resultX = pysicalColumns;
        }
        if(resultY == Document.INIT_Y) {
            resultY = physicalRows;
        }

        logger.debug(String.format("(entryX, entryY) = (%d, %d)", resultX, resultY));

        if(resultX < 2 || resultY <2)
            throw new Exception();
        
        return new int[] { resultX, resultY };
    }

}
