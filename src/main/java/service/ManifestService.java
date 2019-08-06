package service;

import model.Config;
import model.Document;
import model.Resource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ExcelUtil;
import util.FileUtil;
import util.JsonUtil;
import util.PropertiesUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ManifestService {

    private static final Logger logger = LoggerFactory.getLogger(ManifestService.class);

    private static ManifestService instance = new ManifestService();
    DeployService deployService = DeployService.getInstance();
    ResourceService resourceService = ResourceService.getInstance();
    DocumentService documentService = DocumentService.getInstance();

    private ManifestService() {
        logger.debug("create ManifestService");
    }

    public static ManifestService getInstance() {
        return instance;
    }

    /**
     * getManifest
     *
     * @return List<Config>
     * */
    public List<Config> getManifest(String location, String fileName) throws Exception {
        FileUtil.clearDeploy();
        File manifest = new File(String.format("%s/%s", location, fileName));
        String strOfConfig = FileUtil.readFile(manifest);
        return JsonUtil.getArrConfig(strOfConfig);
    }

    /**
     * loadDocumentManifest
     *
     * @param manifest
     * */
    public void loadDocumentManifest(List<Config> manifest) {
        manifest.stream().forEach((config) -> {
            try {
                Document document = documentService.getDocument(config);
                List<Resource> resources = resourceService.convertDocumentToResources(document);
                deployService.deployResource(config, resources);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * loadResourceManifest
     *
     * @param manifest
     * */
    public void loadResourceManifest(List<Config> manifest) throws IOException {
        String dirname = "src/main/resources/sample";
        String filename = "messages";
        String dilimeter = "_";
        List<String> titles = Arrays.asList("ko", "en", "in_ID", "ja", "th_TH", "vi_VN", "zh_CN", "zh_TW");
        String filetype = FileUtil.FILE_FOAMAT_PROPERTIES;
        String excelName = "이름.xlsx";
        String sheetName = "sheet";
        ResourceService resourceService = ResourceService.getInstance();
        DocumentService documentService = DocumentService.getInstance();

        Map<String, List<String>> result = new HashMap<>();
        List<Resource> resources = new ArrayList<>();
        for(int i=0; i<titles.size(); i++) {
            String path = String.format("%s/%s%s%s.%s",
                    dirname, filename, dilimeter, titles.get(i), filetype);
            File file = new File(path);
            // =====================================
            Map<String, String> properties = PropertiesUtil.readPropertiseFile(file);
            Resource resource = new Resource(titles.get(i), properties);
            resources.add(resource);
        }

        //
        XSSFWorkbook book = ExcelUtil.initBook();
        XSSFSheet sheet = ExcelUtil.initSheet(book, sheetName);
        Document document = documentService.convertResourcesToDocument(resources);
        documentService.writeExcelByDocument(sheet, document);

        // =====================================
        FileOutputStream fileoutputstream = new FileOutputStream(
                FileUtil.DEPLOY_LOCATION + "/" + excelName);
        book.write(fileoutputstream);
        fileoutputstream.close();
        // =====================================
    }

}
