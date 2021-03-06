package model;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import util.ExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Document {

    public static final String FIRST_CELL = "ID";
    public static final int INIT_X = -1;
    public static final int INIT_Y = -1;

    int lengthX;
    int lengthY;
    List<String> header;
    List<String> titles;
    Map<String, List<String>> body;

    public Document() {
        this.header = new ArrayList<>();
        this.body = new HashMap<>();
    }

    public Document(int lengthX, int lengthY) {
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.header = new ArrayList<>();
        this.body = new HashMap<>();
    }

}
