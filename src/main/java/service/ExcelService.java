package service;

import model.Config;
import model.Document;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ExcelUtil;
import util.FileUtil;

import java.io.IOException;
import java.util.*;

public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);
    private static ExcelService instance = new ExcelService();

    DeployService deployService = DeployService.getInstance();

    private ExcelService() {
        logger.debug("create ExcelService");
    }

    public static ExcelService getInstance() {
        return instance;
    }

    public XSSFSheet getSheet(String excelName) throws Exception, IOException {
        XSSFWorkbook book = FileUtil.getExcelBook(String.format("%s/%s", FileUtil.TARGET_LOCATION, excelName));
        XSSFSheet xssfSheet = ExcelUtil.getSheet(book, 0);
        return xssfSheet;
    }

    public List<String> getListFromRow(XSSFRow row, int size) {
        List<String> titles = new ArrayList<>();
        for (int cIdx = 0; cIdx < size; cIdx++) {
            XSSFCell cell = ExcelUtil.getCell(row, cIdx);
            if(cell != null) {
                String value = ExcelUtil.getCellValue(cell);
                titles.add(value);
            }
        }
        return titles;
    }

    public Document getMultilingual(Config config) throws Exception, IOException {
        XSSFSheet sheet = getSheet(config.getExcelName());
        int[] lengthXY = ExcelUtil.getLengthXY(sheet);
        Document multilingual = new Document(lengthXY[0], lengthXY[1]);
        List<String> header;
        Map<String, List<String>> body = new HashedMap<>();

        XSSFRow titleRow = ExcelUtil.getRow(sheet, 0);
        header = getListFromRow(titleRow, multilingual.getLengthX());
        multilingual.setHeaderAndTitles(header);

        for(int i=1; i<multilingual.getLengthY(); i++) {
            XSSFRow row = ExcelUtil.getRow(sheet, i);
            List<String> listFromRow = getListFromRow(row, multilingual.getLengthX());
            body.put(listFromRow.remove(0), listFromRow);
        }
        multilingual.setBody(body);
        return multilingual;
    }

    public void createExcelHeader(XSSFSheet sheet, List<String> header) {
        XSSFRow row = ExcelUtil.initRow(sheet, ExcelUtil.FIRST_ROW);
        ExcelUtil.initCells(row, header);
    }

    public void createExcelBody(XSSFSheet sheet) {

    }

}
