import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Administration {

    /**
     * processes the name of a file, which will be the key which will be the key
     * in the hashmap where all dictionaries are kept
     */
    public static String getLanguage(File file) {
        String fileName = file.getName();
        String[] arrOfStr = fileName.split("_", 2);
        return arrOfStr[0];
    }

    /**
     * convert JSON array to list of Word object
     */
    public static Dictionar getDictionaryFromJson(File file) {
        ArrayList<Word> words = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(String.valueOf(file)));
            words = new Gson ().fromJson(reader, new TypeToken<ArrayList<Word>>(){}.getType());
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Dictionar(words);
    }

    /**
      * Receives a folder and creates a list of JSON files,
     * each file represents a dictionary.
     * The dictionary language is extracted from the file name.
     * The method returns a structure containing a hashmmap in which each entry is
     * represented by a dictionary (value) and its language (key).
     */
    public static Database readAllFiles(File folder) {
        HashMap<String, Dictionar> dictionaryMap = new HashMap<> ();

        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            String language = getLanguage(file);
            Dictionar dictionary = getDictionaryFromJson(file);
            dictionaryMap.put(language, dictionary);
        }
        return new Database(dictionaryMap);
    }

    /**
     * Convert JSON string to Word object.
     */
    public static Word readWordJson(File file) {
        Word word = null;
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(String.valueOf(file)));
            word = gson.fromJson(reader,Word.class);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return word;
    }

    /**
     * Convert JSON string to Definition object.
     */
    public static Definition readDefinitionJson(File file) {
        Definition definition = null;
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(String.valueOf(file)));
            definition = gson.fromJson(reader,Definition.class);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return definition;
    }
}
