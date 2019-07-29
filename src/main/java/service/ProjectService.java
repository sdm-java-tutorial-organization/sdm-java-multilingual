package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import util.JsonUtil;
import util.PropertyUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private static ProjectService instance = new ProjectService();

    private ProjectService() {
        logger.debug("create ProjectService");
    }

    public static ProjectService getInstance() {
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

    public List<File> makePackage(String dirName,
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

    public void writePackageByProperties(List<File> listOfFile,
                                         List<String> keys,
                                         List<List<String>> arrOfValues) throws IOException {
        List<StringBuilder> listOfStringBuilder = getEmptyListOfStringBuilder(listOfFile.size());
        for(int i=0; i<keys.size(); i++) {
            for(int j=0; j<arrOfValues.size(); j++) {
                listOfStringBuilder.get(j).append(
                        keys.get(i) + "=" + arrOfValues.get(j).get(i) + "\n");
            }
        }

        for(int i=0; i<listOfFile.size(); i++) {
            FileUtil.writeFile(listOfFile.get(i), listOfStringBuilder.get(i).toString());
        }
    }

    public void writePackageByJson(List<File> listOfFile,
                                   List<String> keys,
                                   List<List<String>> arrOfValues) throws IOException {
        List<Map<String, Object>> listOfJsonMap = getEmptyListOfJsonMap(listOfFile.size());
        for(int i=0; i<keys.size(); i++) {
            for(int j=0; j<arrOfValues.size(); j++) {
                PropertyUtil.recurseCreateMaps(
                        listOfJsonMap.get(j),
                        keys.get(i),
                        arrOfValues.get(j).get(i));
            }
        }

        for(int i=0; i<listOfFile.size(); i++) {
            FileUtil.writeFile(listOfFile.get(i), JsonUtil.mapper.writeValueAsString(listOfJsonMap.get(i)));
        }
    }
}
