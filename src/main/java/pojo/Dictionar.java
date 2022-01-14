package pojo;

import java.util.ArrayList;

public class Dictionar {
    ArrayList<Word> words;

    public Dictionar (ArrayList<Word> words) {
        this.words = words;
    }

    public ArrayList<Word> getWords () {
        return words;
    }

    public void setWords (ArrayList<Word> words) {
        this.words = words;
    }

    @Override
    public String toString () {
        return "pojo.pojo.Dictionar{" +
                "words=" + words +
                '}';
    }
}
