package service;

import model.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ExcelUtil;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    private static ExcelService instance = new ExcelService();

    private ExcelService() {
        logger.debug("create ExcelService");
    }

    public static ExcelService getInstance() {
        return instance;
    }

    public Sheet getSheet(String excelName) throws Exception, IOException {
        XSSFWorkbook book = FileUtil.getExcelBook(String.format("%s/%s", FileUtil.TARGET_LOCATION, excelName));
        XSSFSheet xssfSheet = ExcelUtil.getSheet(book, 0);
        Sheet sheet = new Sheet(xssfSheet);
        ExcelUtil.setEntryXY(sheet);
        return sheet;
    }

    public List<String> getTitles(Sheet sheet, XSSFRow row) {
        List<String> titles = new ArrayList<String>();
        for (int cIdx = 1; cIdx < sheet.getLengthX(); cIdx++) {
            XSSFCell cell = ExcelUtil.getCell(row, cIdx);
            if(cell != null) {
                String value = ExcelUtil.getCellValue(cell);
                titles.add(value);
            }
        }
        return titles;
    }

    public List<List<String>> getValues(Sheet sheet) {
        List<List<String>> arrOfValues = getEmptyValues(sheet.getLengthX());
        for (int rIdx = 1; rIdx < sheet.getLengthY(); rIdx++) {
            XSSFRow row = ExcelUtil.getRow(sheet.getXssfSheet(), rIdx);
            if(row != null) {
                for (int cIdx = 0; cIdx < sheet.getLengthX(); cIdx++) {
                    XSSFCell cell = ExcelUtil.getCell(row, cIdx);
                    if(cell != null) {
                        String value = ExcelUtil.getCellValue(cell);
                        arrOfValues.get(cIdx).add(value);
                    }
                }
            }
        }
        return arrOfValues;
    }

    public List<List<String>> getEmptyValues(int size) {
        List<List<String>> arrOfValues = new ArrayList<>();
        for(int i=0; i<size; i++) {
            arrOfValues.add(new ArrayList<>());
        }
        return arrOfValues;
    }


}
