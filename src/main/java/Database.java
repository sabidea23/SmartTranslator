import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pojo.Definition;
import pojo.Dictionar;
import pojo.Word;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Database {
    private HashMap<String, Dictionar> dictionaryHashMap;
    private static final int INVALID_INDEX = -1;
    private static final int DICTIONARY_FORM = 0;
    private static final int SINGULAR = 1;
    private static final int PLURAL = 2;
    private static final int MAX_SYNONYMS = 3;

    public Database(HashMap<String, Dictionar> database) {
        this.dictionaryHashMap = database;
    }

    public HashMap<String, Dictionar> getDictionaryHashMap () {
        return dictionaryHashMap;
    }

    public void setDictionaryHashMap (HashMap<String, Dictionar> dictionaryHashMap) {
        this.dictionaryHashMap = dictionaryHashMap;
    }

    @Override
    public String toString () {
        return "Database{" +
                "database=" + dictionaryHashMap +
                '}';
    }

    /**
     *  The method is searching in the database for the dictionary corresponding to the language
     *  received as a parameter. If the word does not already exist in the dictionary, it is added.
     */
    public boolean addWord(Word word, String language) throws NullPointerException{
        Dictionar dictionar = getDictionaryHashMap().get(language);
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word.getWord())) {
                return false;
            }
        }
        dictionar.getWords().add(word);
        return true;
    }

    /**
     *  The method is searching in the database for the dictionary corresponding to the language
     *  received as a parameter. If the word exists in the dictionary, it is deleted.
     */
    public boolean removeWord(String word, String language) throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(language);
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word)) {
                dictionar.getWords().remove(wordCompare);
                return true;
            }
        }
        return false;
    }

    /**
     *  The method is searching in the database for the dictionary corresponding to the language
     *  received as a parameter.
     *  If the word does not have the  definition received as parameter, it is added.
     */
    public boolean addDefinitionForWord(String word, String language, Definition definition)
            throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(language);
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word)) {
                for (Definition def:wordCompare.getDefinitions()) {
                    if (def.getDict().equals(definition.getDict())) {
                        return false;
                    }
                }
                wordCompare.getDefinitions().add(definition);
                break;
            }
        }
        return true;
    }

    /**
     *  The method is searching in the database for the dictionary corresponding to the language
     *  received as a parameter.
     *  If the word does have the definition received as parameter, it is removed.
     */
    public boolean removeDefinition(String word, String language, String dictionary)
            throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(language);
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word)) {
                for (Definition def:wordCompare.getDefinitions()) {
                    if (def.getDict().equals (dictionary)) {
                        wordCompare.getDefinitions().remove(def);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Compares a word with those in the dictionary, it returns an index depending on its form (plural,
     * singular or dictionary form). Otherwise, it returns an invalid index.
     */
    public int checkPluralForm(String word, Word wordFromDictionary) {
         if (word.equals(wordFromDictionary.getWord()))
             return DICTIONARY_FORM;
         else if (wordFromDictionary.getSingular().contains(word))
             return SINGULAR;
         else if (wordFromDictionary.getPlural().contains(word))
             return PLURAL;
         else
             return INVALID_INDEX;
    }

    /**
     * Check the form in which the word was found in the dictionary,
     * to know how to translate it.  
     */
    public String checkType(Word wordTranslated, int index) {
        if (index == DICTIONARY_FORM)
            return wordTranslated.getWord();
        else if (index == PLURAL) {
            if (wordTranslated.getType().equals("verb"))
                return wordTranslated.getPlural().get(2);
            else
                return wordTranslated.getPlural().get(0);
        } else {
            if (wordTranslated.getType().equals("verb"))
                return wordTranslated.getSingular().get(2);
            else
                return wordTranslated.getSingular().get(0);
        }
    }

    public String translateWordToEnglish(String word, String fromLanguage) {
        Dictionar dictionary = getDictionaryHashMap().get(fromLanguage);
        for (Word wordCompare: dictionary.getWords()) {
            if (wordCompare.getWord().equals(word)) {
                return wordCompare.getWord_en();
            }
        }
        return word;
    }


    /**
     * If the word is translated from English,  in the database for the dictionary
     *  corresponding to the language received as a parameter.
     */
    public String translateWordFromEnglish(String word, String toLanguage) {
        Dictionar dictionary = getDictionaryHashMap().get(toLanguage);
        for (Word wordCompare: dictionary.getWords()) {
            if (wordCompare.getWord_en ().equals(word)) {
                return wordCompare.getWord();
            }
        }
        return word;
    }

    public String translateWord(String word, String fromLanguage, String toLanguage) {
        if (fromLanguage.equals("en"))
            return translateWordFromEnglish(word, toLanguage);

        if (toLanguage.equals("en"))
            return translateWordToEnglish(word, fromLanguage);

        Dictionar dictionary = getDictionaryHashMap().get(fromLanguage);
        for (Word wordCompare: dictionary.getWords()) {
            int index = checkPluralForm(word, wordCompare);
            if (index != INVALID_INDEX) { //s-a gasit cuvantul in dictionar si se siie si locul

                Dictionar dictionaryToTranslate = getDictionaryHashMap().get(toLanguage);
                for (Word wordTranslate : dictionaryToTranslate.getWords ()) {
                    if (wordCompare.getWord_en().equals (wordTranslate.getWord_en())) {
                        return checkType(wordTranslate, index);
                        //aici trebuie returnat in fucntie de unde a fost gasit in primul dictionar
                    }
                }
            }
        }
        return word;
    }

    public String translateSentence(String sentence, String fromLanguage, String toLanguage) {
        String []arrStr = sentence.split(" ");

        String translatedSentence = "";
         for (int i = 0; i < arrStr.length; i++) {

             String translateWord = translateWord(arrStr[i].toLowerCase(), fromLanguage, toLanguage);
             if (i == 0) {
                 translateWord = translateWord.substring(0,1).toUpperCase() + translateWord.substring(1).toLowerCase();
                 translatedSentence =  translatedSentence.concat(translateWord);
                 continue;
             }
             translatedSentence =  translatedSentence.concat(" " + translateWord);
         }
         return translatedSentence;
    }

    /**
     * Returns a list of synonyms of a word.
     */
    public ArrayList<String> getSynonyms(String translateWord, String toLanguage) throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(toLanguage);
        ArrayList<String> listOfSynonyms = new ArrayList<String>();
        for (Word word:dictionar.getWords()) {
            if (word.getWord().equals(translateWord)) {
                for (Definition definitions : word.getDefinitions()) {
                    if (definitions.getDictType().equals("synonyms")) {
                        listOfSynonyms.addAll(definitions.getText());
                    }
                }
            }
        }
        return listOfSynonyms;
    }

    public String getDictionaryForm(String translatedWord, String toLanguage) throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(toLanguage);
        for (Word word:dictionar.getWords()) {
            if (word.getWord().equals(translatedWord) || word.getPlural().contains(translatedWord) ||
                word.getSingular().contains(translatedWord)) {
                return word.getWord();
            }
        }
        return translatedWord;
    }

    public int getMaxSynonyms(HashMap<String, ArrayList<String>> synonyms) {
        int numberMaxSynonyms = 0;
        for (Map.Entry<String, ArrayList<String>> set : synonyms.entrySet()) {
            for (String str : set.getValue())
                numberMaxSynonyms++;
        }

        return Math.min(numberMaxSynonyms, MAX_SYNONYMS);
    }

    public ArrayList<String> translateSentences(String sentence, String fromLanguage,
                                                String toLanguage) throws NullPointerException {
        String []arrStr = sentence.split (" ");
        HashMap<String, ArrayList<String>> synonyms = new HashMap<>();
        ArrayList<String>  sentences = new ArrayList<String>();

        for (String str:arrStr) {
            String translateWord = translateWord(str.toLowerCase(), fromLanguage, toLanguage);
            String dictionaryForm = getDictionaryForm(translateWord, toLanguage);
            synonyms.put(dictionaryForm, getSynonyms(dictionaryForm, toLanguage));
        }

        int maxSynonyms = getMaxSynonyms(synonyms);
        for (int i = 0; i < maxSynonyms; i++) {
            String translatedSentence = "";
            for (Map.Entry<String,ArrayList<String>> entry : synonyms.entrySet()) {
                String synonym = "";
                if (entry.getValue().isEmpty())
                    synonym = entry.getKey();
                else if (maxSynonyms > entry.getValue().size())
                    synonym = entry.getValue().get(0);
                else
                    synonym = synonyms.get(entry.getKey()).get(i);
                
                translatedSentence =  translatedSentence.concat(synonym + " ");
            }
            translatedSentence = translatedSentence.substring(0,1).toUpperCase() +
                    translatedSentence.substring(1).toLowerCase();

            sentences.add(translatedSentence);
        }
        return sentences;
    }

    public ArrayList<Definition> getDefinitionsForWord(String word, String language)
            throws NullPointerException{
        Dictionar dictionar = getDictionaryHashMap().get(language);
        ArrayList<Definition> definitions = new ArrayList<Definition>();
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word)) {
                definitions.addAll(wordCompare.getDefinitions());
            }
        }

        if (definitions.isEmpty()) {
            System.out.println("The word " + word + " is not found in the dictionary");
            return null;
        }
        definitions.sort(Comparator.comparing(Definition::getYear));
        return definitions;
    }

    /**
     *  Constructs a FileWriter given a file name, using the platform's default charset.
     */
    public void writeJson(JSONObject obj) {
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
    public void sortWords(JSONArray words) {
        Collections.sort(words, new Comparator() {
            private static final String KEY_NAME = "word";
            public int compare (Object o1, Object o2) {
                JSONObject obj1 = (JSONObject) o1;
                JSONObject obj2 = (JSONObject) o2;
                String name1 = (String) obj1.get(KEY_NAME);
                String name2 = (String) obj2.get(KEY_NAME);
                return name1.compareTo(name2);
            }
        });
    }

    /**
     * Sorts a list of JSONS by year in ascending order.
     */
    public void sortDefinitions(JSONArray definitions) {
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

    /**
     * Exports a JSON dictionary,  in which the words are sorted alphabetically and the definitions are
     * sorted in ascending order by the year of publication of each dictionary.
     */
    @SuppressWarnings("unchecked")
    public void exportDictionary(String language) throws Exception {
        Dictionar dictionar = getDictionaryHashMap().get(language);

        JSONObject obj = new JSONObject();
        JSONArray words = new JSONArray();

        for (Word wordFromDictionary:dictionar.getWords()) {
            JSONObject word = new JSONObject();
            word.put("word", wordFromDictionary.getWord());
            word.put("word_en", wordFromDictionary.getWord_en());
            word.put("type", wordFromDictionary.getType());
            word.put("singular", wordFromDictionary.getSingular());
            word.put("plural", wordFromDictionary.getPlural());

            JSONArray definitions = new JSONArray();
            for (Definition definitionFromDictionary: wordFromDictionary.getDefinitions()) {
                JSONObject definition = new JSONObject();
                definition.put("dict", definitionFromDictionary.getDict());
                definition.put("dicType", definitionFromDictionary.getDictType());
                definition.put("year", definitionFromDictionary.getYear());
                JSONArray texts = new JSONArray();
                texts.addAll (definitionFromDictionary.getText());
                definition.put("text", texts);
                definitions.add(definition);
                sortDefinitions(definitions);
            }
            word.put("definitions", definitions);
            words.add(word);
        }
        sortWords(words);
        obj.put("Dictionary", words);
        writeJson(obj);
    }

    public static void main (String[] args) {
        try {
            Database database = Administration.readAllFiles(new File ("init"));
            Features.printMap(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}