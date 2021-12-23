import pojo.Dictionar;
import pojo.Word;

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
}
