
import model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExcelService;
import service.ManifestService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        ManifestService manifestService = ManifestService.getInstance();
        List<Config> manifest = manifestService.getManifest();
        manifestService.loadManifest(manifest);
        // ---------------------------------------------------------------

        // ---------------------------------------------------------------
        // 잠깐테스트
        /*makeExcelFromProperties(
                "src/main/resources/sample",
                "messages",
                "_",
                Arrays.asList("ko"),
                FileUtil.FILE_FOAMAT_PROPERTIES);*/
        // ---------------------------------------------------------------

        logger.debug("process end...");
    }

    public static void makeExcelFromProperties(String dirname,
                                               String filename,
                                               String dilimeter,
                                               List<String> titles,
                                               String filetype) throws IOException {
        Map<String, Map<String, String>> result = new HashMap<>();
        titles.stream().forEach((title) -> {
            try {
                String path = String.format("%s/%s%s%s.%s",
                        dirname,
                        filename,
                        dilimeter,
                        title,
                        filetype);
                File file = new File(path);
                Properties properties = new Properties();
                properties.load(new BufferedReader(new FileReader(file)));
                Map<String, String> map = (Map) properties;

                map.entrySet().forEach((key) -> {
                    System.out.println(key);
                });

                /*System.out.println(strOfConfig);*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
