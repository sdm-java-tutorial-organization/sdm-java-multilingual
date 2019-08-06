package service;

import model.Config;
import model.Multilingual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import util.JsonUtil;
import util.PropertyUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        List<StringBuilder> listOfStringBuilder = new ArrayList<StringBuilder>();
        for(int i=0; i<size; i++) {
            listOfStringBuilder.add(new StringBuilder());
        }
        return listOfStringBuilder;
    }

    private static List<Map<String, Object>> getEmptyListOfJsonMap(int size) {
        List<Map<String, Object>> listOfJsonMap = new ArrayList<>();
        for(int i=0; i<size; i++) {
            listOfJsonMap.add(new TreeMap<>());
        }
        return listOfJsonMap;
    }

    public List<File> createDeployPackage(String dirName,
                                  String fileName,
                                  String dilimeter,
                                  String type,
                                  List<String> reqTitleNames,
                                  List<String> excelTitleNames) throws IOException {
        FileUtil.createDirectory(dirName);
        return FileUtil.createFiles(dirName,
                fileName,
                dilimeter,
                type,
                reqTitleNames,
                excelTitleNames);
    }

    public void writeFilesByProperties(List<File> listOfFile,
                                         List<String> keys,
                                         List<List<String>> values) throws IOException {
        List<StringBuilder> listOfStringBuilder = getEmptyListOfStringBuilder(listOfFile.size());
        for(int row=0; row<keys.size(); row++) {
            for(int column=0; column<listOfFile.size(); column++) {
                String line = keys.get(row) + "=" + values.get(row).get(column) + "\n";
                listOfStringBuilder.get(column).append(line);
            }
        }

        for(int column=0; column<listOfFile.size(); column++) {
            FileUtil.writeFile(listOfFile.get(column), listOfStringBuilder.get(column).toString());
        }
    }

    public void writeFilesByJson(List<File> listOfFile,
                                   List<String> keys,
                                   List<List<String>> values) throws IOException {
        List<Map<String, Object>> listOfJsonMap = getEmptyListOfJsonMap(listOfFile.size());
        for(int row=0; row<keys.size(); row++) {
            for(int column=0; column<listOfFile.size(); column++) {
                PropertyUtil.recurseCreateMaps(
                        listOfJsonMap.get(column), keys.get(row), values.get(row).get(column));
            }
        }

        for(int row=0; row<listOfFile.size(); row++) {
            FileUtil.writeFile(listOfFile.get(row), JsonUtil.mapper.writeValueAsString(listOfJsonMap.get(row)));
        }
    }

    public void deploy(Config config, Multilingual multilingual) throws IOException {
        List<File> listOfFile = createDeployPackage(
                config.getDirName(),
                config.getFilePrefix(),
                config.getDilimeter(),
                config.getType(),
                config.getTitles(),
                multilingual.getTitles());

        switch (config.getType()) {
            case FileUtil.FILE_FOAMAT_JSON:
                writeFilesByJson(listOfFile,
                        new ArrayList<>(multilingual.getBody().keySet()),
                        new ArrayList<>(multilingual.getBody().values()));
                break;
            case FileUtil.FILE_FOAMAT_PROPERTIES:
                writeFilesByProperties(listOfFile,
                        new ArrayList<>(multilingual.getBody().keySet()),
                        new ArrayList<>(multilingual.getBody().values()));
                break;
        }
    }
}
