package pojo;

import java.util.ArrayList;
import java.util.Objects;

public class Word {
    private String word;
    private String word_en;
    private String type;
    private ArrayList<String> singular;
    private ArrayList<String> plural;
    private ArrayList<Definition> definitions;

    public Word(String word, String wordEnglish, String type, ArrayList<String> singular, ArrayList<String> plural,
                ArrayList<Definition> definition) {
        this.word = word;
        this.word_en = wordEnglish;
        this.type = type;
        this.singular = singular;
        this.plural = plural;
        this.definitions = definition;
    }

    public String getWord () {
        return word;
    }

    public void setWord (String word) {
        this.word = word;
    }

    public String getWord_en () {
        return word_en;
    }

    public void setWord_en (String wordEnglish) {
        this.word_en = wordEnglish;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public ArrayList<String> getSingular () {
        return singular;
    }

    public void setSingular (ArrayList<String> singular) {
        this.singular = singular;
    }

    public ArrayList<String> getPlural () {
        return plural;
    }

    public void setPlural (ArrayList<String> plural) {
        this.plural = plural;
    }

    public ArrayList<Definition> getDefinitions () {
        return definitions;
    }

    public void setDefinitions (ArrayList<Definition> definition) {
        this.definitions = definition;
    }

    @Override
    public String toString () {
        return "pojo.pojo.Word{" +
                "word='" + word + '\'' +
                ", wordEnglish='" + word_en + '\'' +
                ", type='" + type + '\'' +
                ", singular=" + singular +
                ", plural=" + plural +
                ", definition=" + definitions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Word word1 = (Word) o;
        return Objects.equals(getWord(), word1.getWord()) && Objects.equals(getWord_en(), word1.getWord_en())
                && Objects.equals(getType(), word1.getType());
    }
}
