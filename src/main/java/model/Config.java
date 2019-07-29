package model;

import lombok.Data;

import java.util.List;

@Data
public class Config {
    String deployPath;
    String excelName;
    String dirName;
    String filePrefix;
    String dilimeter;
    List<String> titles;
    String type;
}
