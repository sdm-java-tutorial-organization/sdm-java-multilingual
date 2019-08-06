package service;

import model.Config;
import model.Document;
import model.Resource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ExcelUtil;
import util.FileUtil;
import util.PropertiesUtil;

import java.io.IOException;
import java.util.*;

public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private static DocumentService instance = new DocumentService();

    private DocumentService() {
        logger.debug("create DocumentService");
    }

    public static DocumentService getInstance() {
        return instance;
    }

    /**
     * getDocument
     *
     * @param config - 구성요소
     * */
    public Document getDocument(Config config) throws Exception, IOException {
        logger.debug("call getDocument");

        // TODO file type 분리
        XSSFWorkbook book = ExcelUtil.getBook(config.getImportPath(), config.getDocument().getFile());
        XSSFSheet sheet = ExcelUtil.getSheet(book, 0);
        int[] lengthXY = ExcelUtil.getLengthXY(sheet); // (*)
        Document document = new Document(lengthXY[0], lengthXY[1]);
        List<String> header = document.getHeader();
        Map<String, List<String>> body = document.getBody();

        // header
        XSSFRow titleRow = ExcelUtil.getRow(sheet, 0);
        header = ExcelUtil.getListByRow(titleRow, document.getLengthX());
        document.setHeader(header);

        // body
        for(int i=1; i<document.getLengthY(); i++) {
            XSSFRow row = ExcelUtil.getRow(sheet, i);
            List<String> listFromRow = ExcelUtil.getListByRow(row, document.getLengthX());
            body.put(listFromRow.remove(0), listFromRow);
        }

        return document;
    }

    /**
     * convertResourcesToDocument
     *
     * @param resources
     * @return Document
     * */
    public Document convertResourcesToDocument(List<Resource> resources) {
        logger.debug("call convertResourcesToDocument");

        Document document = new Document();
        List<String> header = document.getHeader();
        Map<String, List<String>> body = document.getBody();
        header.add(Document.FIRST_CELL);
        for(int resIdx=0; resIdx<resources.size(); resIdx++) {
            Resource resource = resources.get(resIdx);
            String title = resource.getTitle();
            Map<String, String> properties = resource.getProperties();
            header.add(title);
            PropertiesUtil.recurseCreateLists(body, properties, resIdx);
        }
        return document;
    }

    /**
     * writeExcelByDocument
     *
     * @param sheet
     * @param document
     * @return void
     * */
    public void writeExcelByDocument(XSSFSheet sheet,
                                     Document document) {
        logger.debug("call writeExcelByDocument");

        List<String> header = document.getHeader();
        Map<String, List<String>> body = document.getBody();

        // header
        XSSFRow titleRow = ExcelUtil.initRow(sheet, 0);
        ExcelUtil.initCells(titleRow, header);

        // body
        List<Map.Entry<String, List<String>>> entryList = new ArrayList<>(body.entrySet());
        for(int i=0; i<entryList.size(); i++) {
            Map.Entry<String, List<String>> entry = entryList.get(i);
            String key = entry.getKey();
            LinkedList<String> values = (LinkedList<String>) entry.getValue();
            XSSFRow row = ExcelUtil.initRow(sheet, i+1);
            values.addFirst(key);
            ExcelUtil.initCells(row, values);
        }
    }



}
