package model;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import util.ExcelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Multilingual {

    public static final int INIT_X = -1;
    public static final int INIT_Y = -1;

    int lengthX;
    int lengthY;
    final int COUNT_OF_LANGUAGE;
    List<String> header;
    Map<String, List<String>> body;

    public Multilingual(int lengthX, int lengthY) {
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.COUNT_OF_LANGUAGE = lengthX - 1;
    }

}
