package model;

public class MultilingualLine {
    String key;
    String[] values;

    public MultilingualLine (String key, int size) {
        this.key = key;
        this.values = new String[size];
    }

    public void addValue(int index, String value) {
        values[index] = value;
    }
}
