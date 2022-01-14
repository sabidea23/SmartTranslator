package pojo;

import java.util.ArrayList;
import java.util.Objects;

public class Definition {
    private String dict;
    private String dictType;
    private int year;
    private ArrayList<String> text;

    public Definition(String dictName, String dictType, int year, ArrayList<String> text) {
        this.dict = dictName;
        this.dictType = dictType;
        this.year = year;
        this.text = text;
    }

    public String getDict () {
        return dict;
    }

    public void setDict (String dict) {
        this.dict = dict;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "pojo.pojo.Definition{" +
                "dictName='" + dict + '\'' +
                ", dictType='" + dictType + '\'' +
                ", year=" + year +
                ", text=" + text +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Definition that = (Definition) o;
        return getYear() == that.getYear() && Objects.equals(getDict(), that.getDict()) &&
                Objects.equals(getDictType(), that.getDictType());
    }
}
