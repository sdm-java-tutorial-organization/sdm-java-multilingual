package model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Config {
    String importPath;
    String exportPath;
    ConfigDocument document;
    ConfigResource resource;

    @Data
    public static class ConfigDocument {
        String dir;
        String file;
        String type;
    }

    @Data
    public static class ConfigResource{
        String dir;
        String file;
        String dilimeter;
        List<String> titles;
        String type;
    }
}
