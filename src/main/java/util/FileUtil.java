package util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static final String DEPLOY_LOCATION = "src/main/resources/deploy";
    public static final String TARGET_LOCATION = "src/main/resources/target";
    public static final String CONFIG_LOCATION = "src/main/resources/config";
    public static final String FILE_MANIFEST = "manifest";
    public static final String FILE_FOAMAT_PROPERTIES = "properties";
    public static final String FILE_FOAMAT_JSON = "json";

    /**
     * getExcelBook
     *
     * @param path - 엑셀파일경로
     * */
    public static XSSFWorkbook getExcelBook(String path) throws IOException {
        logger.debug("read excel : " + path);
        File relativeFile = new File(path);
        File absoluteFile = new File(relativeFile.getAbsolutePath());
        FileInputStream fis = new FileInputStream(absoluteFile);
        return new XSSFWorkbook(fis);
    }

    /**
     * clearDeploy - 배포폴더 초기화
     *
     * */
    public static void clearDeploy() {
        logger.debug("start clearing deploy dir");
        File dir = new File(DEPLOY_LOCATION);
        for(File file: dir.listFiles()) {
            if(file.isDirectory()) {
                for(File innerfile: file.listFiles()) {
                    logger.debug("delete file : " + innerfile.getName());
                    innerfile.delete();
                }
                logger.debug("delete file : " + file.getName());
                file.delete();
            } else {
                logger.debug("delete file : " + file.getName());
                file.delete();
            }
        }
    }

    /**
     * createDirectory
     *
     * @param name
     * */
    public static File createDirectory(String name) {
        logger.debug("create dir : " + name);
        File file = new File(String.format("%s/%s", DEPLOY_LOCATION, name));
        file.mkdir();
        return file;
    }

    public static File createFile(String name) throws IOException {
        logger.debug("create file : " + name);
        File file = new File(String.format("%s/%s", DEPLOY_LOCATION, name));
        file.createNewFile();
        return file;
    }

    /**
     * createFiles
     *
     * @param dirName - 폴더이름
     * @param fileName - 파일이름 (접두사)
     * @param dilimeter - 구분자
     * @param type - 배포타입
     * @param reqTitleNames - 요청 그룹명
     * @param excelTitleNames - 엑셀 첫번째 행 그룹명
     * */
    public static List<File> createFiles(
            String dirName,
            String fileName,
            String dilimeter,
            String type,
            List<String> reqTitleNames,
            List<String> excelTitleNames) throws IOException {
        List<File> files = new ArrayList<File>();
        for(int i=0; i<excelTitleNames.size(); i++) {
            StringBuilder sb = new StringBuilder();
            String entryName = sb
                    .append(dirName).append("/").append(fileName).append(dilimeter)
                    .append(reqTitleNames.get(i) != null ? reqTitleNames.get(i) : excelTitleNames.get(i))
                    .append(".").append(type.equals("json") ? FileUtil.FILE_FOAMAT_JSON : FileUtil.FILE_FOAMAT_PROPERTIES)
                    .toString();
            File newFile = FileUtil.createFile(entryName);
            files.add(newFile);
        }
        return files;
    }

    /**
     * writeFile
     *
     */
    public static void writeFile(File file, String contents) throws IOException {
        logger.debug("writeFile : " + file.getAbsolutePath());
        FileWriter fw = new FileWriter(file);
        fw.write(contents);
        fw.close();
    }

    /**
     * readFile
     *
     */
    public static String readFile(File file) throws IOException {
        logger.debug("readFile : " + file.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

}
