package service;

import model.Config;
import model.Document;
import model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import util.JsonUtil;
import util.PropertiesUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DeployService {

    private static final Logger logger = LoggerFactory.getLogger(DeployService.class);

    private static DeployService instance = new DeployService();

    private DeployService() {
        logger.debug("create DeployService");
    }

    public static DeployService getInstance() {
        return instance;
    }

    private static List<StringBuilder> getEmptyListOfStringBuilder(int size) {
        logger.debug("call getEmptyListOfStringBuilder");

        List<StringBuilder> listOfStringBuilder = new ArrayList<StringBuilder>();
        for(int i=0; i<size; i++) {
            listOfStringBuilder.add(new StringBuilder());
        }
        return listOfStringBuilder;
    }

    public List<File> createDeployPackage(String dirName,
                                  String fileName,
                                  String dilimeter,
                                  String type,
                                  List<String> reqTitleNames,
                                  List<String> excelTitleNames) throws IOException {
        logger.debug("call createDeployPackage");
        FileUtil.createDirectory(dirName);
        return FileUtil.createFiles(dirName,
                fileName,
                dilimeter,
                type,
                reqTitleNames,
                excelTitleNames);
    }

    /**
     * deployResource
     *
     * @param config
     * @param resources
     * */
    public void deployResource(Config config,
                       List<Resource> resources) throws IOException {

        List<String> titles = resources
                .stream()
                .map(resource -> resource.getTitle())
                .collect(Collectors.toList());

        List<File> files = createDeployPackage(
                config.getResource().getDir(),
                config.getResource().getFile(),
                config.getResource().getDilimeter(),
                config.getResource().getType(),
                config.getResource().getTitles(),
                titles);

        for(int i=0; i<files.size(); i++) {
            switch (config.getResource().getType()) {
                case FileUtil.FILE_FOAMAT_JSON:
                    FileUtil.writeFile(files.get(i), resources.get(i).toJsonString());
                    break;
                case FileUtil.FILE_FOAMAT_PROPERTIES:
                    FileUtil.writeFile(files.get(i), resources.get(i).toString());
                    break;
            }
        }
    }

}
