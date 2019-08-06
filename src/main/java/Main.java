
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExcelService;
import service.PropertiesService;
import util.ExcelUtil;
import util.FileUtil;

import java.io.*;
import java.util.*;


/**
 * STEP1. MANIFEST 읽기
 * STEP2. S3읽기 (EXCEL)
 * STEP3. 파일읽기
 * STEP4. 파일쓰기
 * STEP5. S3배포
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.debug("process start...");

        // ---------------------------------------------------------------
        /*ManifestService manifestService = ManifestService.getInstance();
        List<Config> manifest = manifestService.getManifest();
        manifestService.loadManifest(manifest);*/
        // ---------------------------------------------------------------

        // ---------------------------------------------------------------
        // 잠깐테스트
        makeExcelFromProperties(
                "src/main/resources/sample",
                "messages",
                "_",
                Arrays.asList("ko", "en", "in_ID", "ja", "th_TH", "vi_VN", "zh_CN", "zh_TW"),
                FileUtil.FILE_FOAMAT_PROPERTIES);
        // ---------------------------------------------------------------

        logger.debug("process end...");
    }

    public static void makeExcelFromProperties(String dirname,
                                               String filename,
                                               String dilimeter,
                                               List<String> titles,
                                               String filetype) throws IOException {
        PropertiesService propertiesService = PropertiesService.getInstance();
        ExcelService excelService = ExcelService.getInstance();

        Map<String, List<String>> result = new HashMap<>();
        for(int i=0; i<titles.size(); i++) {
            // =====================================
            String path = String.format("%s/%s%s%s.%s",
                    dirname, filename, dilimeter, titles.get(i), filetype);
            File file = new File(path);
            // =====================================
            Map<String, String> propertiesMap = propertiesService.getPropertiseMapFromFile(file);
            // =====================================
            propertiesService.accumulateExcel(result, propertiesMap, i);
            // =====================================
        }

        // =====================================
        XSSFWorkbook book = ExcelUtil.initBook();
        XSSFSheet sheet = ExcelUtil.initSheet(book, "sheet");

        excelService.createExcelHeader(sheet, null);

        List<Map.Entry<String, List<String>>> entryList = new ArrayList<>(result.entrySet());
        for(int i=0; i<entryList.size(); i++) {
            Map.Entry<String, List<String>> entry = entryList.get(i);
            XSSFRow row = ExcelUtil.initRow(sheet, i);
            String key = entry.getKey();
            LinkedList<String> value = (LinkedList<String>) entry.getValue();
            value.addFirst(key);
            /*List<String> cells = new ArrayList<>();
            cells.add(key);
            titles.stream().forEach(title -> {
                cells.add(value.get(title) != null ? value.get(title) : "");
            });*/
            ExcelUtil.initCells(row, value);
        }

        // =====================================

        // =====================================
        FileOutputStream fileoutputstream = new FileOutputStream(
                FileUtil.DEPLOY_LOCATION + "/이름.xlsx");
        book.write(fileoutputstream);
        fileoutputstream.close();
        // =====================================
    }


}
