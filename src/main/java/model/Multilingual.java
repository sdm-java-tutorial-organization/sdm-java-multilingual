package model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Multilingual {

    int lengthX;
    int lengthY;
    final int COUNT_OF_LANGUAGE;
    List<String> header;
    Map<String, MultilingualLine> body;
    /*List<MultilingualLine> body;*/

    public Multilingual(int lengthX, int lengthY) {
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        COUNT_OF_LANGUAGE = lengthX - 1;
    }

}
