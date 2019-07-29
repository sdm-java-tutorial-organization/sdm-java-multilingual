package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFSheet;

@Data
@AllArgsConstructor
public class Sheet {

    public static final int INIT_X = -1;
    public static final int INIT_Y = -1;

    XSSFSheet xssfSheet;
    int lengthX = INIT_X;
    int lengthY = INIT_Y;

    public Sheet(XSSFSheet xssfSheet) {
        this.xssfSheet = xssfSheet;
    }
}
