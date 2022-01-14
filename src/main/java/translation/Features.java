package translation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pojo.Dictionar;
import pojo.Word;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Features {

    /**
     * Print each key + value entry in the database and messages
     * if a dictionary is empty or there are no dictionaries.
     */
    public static void printMap(Database database) {
        if (database.getDictionaryHashMap().isEmpty()) {
            System.out.println("There are no dictionaries!");
            return;
        }

        for (HashMap.Entry<String, Dictionar> entry : database.getDictionaryHashMap().entrySet()) {
            if (entry.getValue().getWords().isEmpty()) {
                System.out.println("There are no words in the dictionary!");
                return;
            }
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }
    }

    /**
     * Print all words in a dictionary and a message in case the dictionary is empty.
     */
    public static void printDictionary(Database database, String language) {
        Dictionar dictionar = database.getDictionaryHashMap().get(language);
        if (dictionar.getWords().isEmpty()) {
            System.out.println("There are no words in the dictionary!");
            return;
        }

        System.out.println(language + " ");
        for (Word word: dictionar.getWords()) {
            System.out.println(word);
        }
    }

    /**
     *  Constructs a FileWriter given a file name, using the platform's default charset.
     */
    public static void writeJson(JSONObject obj) {
        FileWriter file = null;
        try {
            file = new FileWriter("tests\\exportJson\\test1.json");
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert file != null;
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sorts a list of JSONS by name in alphabetical order.
     */
    @SuppressWarnings("unchecked")
    public static void sortWords(JSONArray words) {
        words.sort (new Comparator () {
            private static final String KEY_NAME = "word";

            public int compare (Object o1, Object o2) {
                JSONObject obj1 = (JSONObject) o1;
                JSONObject obj2 = (JSONObject) o2;
                String name1 = (String) obj1.get (KEY_NAME);
                String name2 = (String) obj2.get (KEY_NAME);
                return name1.compareTo (name2);
            }
        });
    }

    /**
     * Sorts a list of JSONS by year in ascending order.
     */
    @SuppressWarnings("unchecked")
    public static void sortDefinitions(JSONArray definitions) {
        Collections.sort(definitions, new Comparator() {
            private static final String KEY_YEAR = "year";
            public int compare (Object o1, Object o2) {
                JSONObject obj1 = (JSONObject) o1;
                JSONObject obj2 = (JSONObject) o2;
                Integer year1 = (Integer) obj1.get(KEY_YEAR);
                Integer year2 = (Integer) obj2.get(KEY_YEAR);
                return year1.compareTo(year2);
            }
        });
    }

}
