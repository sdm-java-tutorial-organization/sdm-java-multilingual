
import model.Config;
import model.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExcelService;
import service.ManifestService;
import service.ProjectService;
import util.ExcelUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;


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
        ManifestService manifestService = ManifestService.getInstance();
        List<Config> manifest = manifestService.getManifest();

        manifest.stream().forEach((config) -> {
            try {
                manageExcel(config);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        logger.debug("process end...");
    }

    /**
     * manageExcel
     *
     * @param config - 구성파일
     * */
    public static void manageExcel(Config config) throws Exception, IOException {
        ProjectService projectService = ProjectService.getInstance();
        ExcelService excelService = ExcelService.getInstance();
        Sheet sheet = excelService.getSheet(config.getExcelName());

        List<String> titles;
        List<String> keys;
        List<List<String>> arrOfValues;

        XSSFRow titleRow = ExcelUtil.getRow(sheet.getXssfSheet(), 0);
        titles = excelService.getTitles(sheet, titleRow);
        arrOfValues = excelService.getValues(sheet);
        keys = arrOfValues.remove(0);

        List<File> listOfFile = projectService.makePackage(config.getDirName(),
                config.getFilePrefix(),
                config.getDilimeter(),
                config.getType(),
                config.getTitles(),
                titles);
        if(config.getType().equals("json")) {
            projectService.writePackageByJson(listOfFile, keys, arrOfValues);
        } else {
            projectService.writePackageByProperties(listOfFile, keys, arrOfValues);
        }
    }

}
