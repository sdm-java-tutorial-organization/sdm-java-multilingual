package service;

import model.Config;
import model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ManifestService {

    private static final Logger logger = LoggerFactory.getLogger(ManifestService.class);

    private static ManifestService instance = new ManifestService();

    ExcelService excelService = ExcelService.getInstance();
    DeployService deployService = DeployService.getInstance();

    private ManifestService() {
        logger.debug("create ManifestService");
    }

    public static ManifestService getInstance() {
        return instance;
    }

    public List<Config> getManifest() throws Exception {
        FileUtil.clearDeploy();
        File manifest = new File(String.format("%s/%s.%s",
                FileUtil.CONFIG_LOCATION,
                FileUtil.FILE_MANIFEST,
                FileUtil.FILE_FOAMAT_JSON));
        String strOfConfig = FileUtil.readFile(manifest);
        return JsonUtil.getArrConfig(strOfConfig);
    }

    public void loadManifest(List<Config> manifest) {
        manifest.stream().forEach((config) -> {
            try {
                Document multilingual = excelService.getMultilingual(config);
                deployService.deploy(config, multilingual);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
