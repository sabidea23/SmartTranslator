import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    public void setDictionaryHashMap(HashMap<String, Dictionar> dictionaryHashMap) {
        this.dictionaryHashMap = dictionaryHashMap;
    }

    @Override
    public String toString () {
        return "Database{" +
                "database=" + dictionaryHashMap +
                '}';
    }

    /**
     * If the new word has new definitions, they are added to the dictionary word.
     * If the dictionary definition already exists, check if the new word has more definitions
     * or synonyms than the one in the dictionary. If a new one is found, it is added to the
     * list of definitions of the that dictionary.
     */
    public boolean checkDefinitions(Word wordFromDictionary, Word newWord) {
        boolean wasModified = false;
        for (int i = 0; i < newWord.getDefinitions().size(); i++) {
            if (!wordFromDictionary.getDefinitions().contains(newWord.getDefinitions().get(i))) {
                wordFromDictionary.getDefinitions().add(newWord.getDefinitions().get(i));
                return true;
            } else {  
                for (int j = 0; j < newWord.getDefinitions().get(i).getText().size(); j++) {
                    if (!wordFromDictionary.getDefinitions().get(i).getText().
                            contains(newWord.getDefinitions().get(i).getText().get(i))) {
                        wordFromDictionary.getDefinitions().get(i).getText().add(newWord.getDefinitions().get(i).
                                getText().get(i));
                        return true;
                    }
                }
            }
        }
        return wasModified;
    }

    /**
     *  The method is searching in the database for the dictionary corresponding to the language
     *  received as a parameter. If the word does not already exist in the dictionary, it is added.
     *  If the new word has new definitions, they are added to the dictionary word.
     */
    public boolean addWord(Word word, String language) throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(language);
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word.getWord())) {
                return checkDefinitions(wordCompare, word);
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
     * Check for new synonyms or definitions in the new dictionary and add them to the
     * database dictionary.
     */
    public boolean checkDefinitions(Definition wordFromDictionary, Definition newDefinition) {
        boolean isModified = false;
        for (int i = 0; i < newDefinition.getText().size(); i++) {
            if (!wordFromDictionary.getText().contains(newDefinition.getText().get(i))) {
                wordFromDictionary.getText().add(newDefinition.getText().get(i));
                isModified = true;
            }
        }
        return isModified;
    }

    /**
     *  The method is searching in the database for the dictionary corresponding to the language
     *  received as a parameter.
     *  If the word does not have the  definition received as parameter, it is added.
     *  If it has new definitions or synonyms, they are added to the database.
     */
    public boolean addDefinitionForWord(String word, String language, Definition definition)
                                        throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(language);
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word)) {
                for (Definition def:wordCompare.getDefinitions()) {
                    if (def.getDict().equals(definition.getDict())) {
                        return checkDefinitions(def, definition);
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
                for (Definition def : wordCompare.getDefinitions()) {
                    if (def.getDict().equals(dictionary)) {
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

    /**
     * If the translation is done in English, we look up the word received as a parameter in
     * the dictionary of the language from which it comes and return the form to the English language.
     */
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
     * If the translation is made from English, we look in the dictionary of the language in which
     * it is translated if we find a word with the same translation in English, and we return
     * its infinitive form
     */
    public String translateWordFromEnglish(String word, String toLanguage) {
        Dictionar dictionary = getDictionaryHashMap().get(toLanguage);
        for (Word wordCompare: dictionary.getWords()) {
            if (wordCompare.getWord_en().equals(word)) {
                return wordCompare.getWord();
            }
        }
        return word;
    }

    /**
     * Translate a word from one language to another.
     * The causes of translation from and into English are taken into account.
     * The method is searching in the database for the dictionary corresponding
     * to the language from which the translation is made. Look up the word in
     * the dictionary and the form in which it was found (singular, plural) to
     * know how to translate it. Look up in the dictionary of the language in which the
     * translation is made the word that has the same translation
     * in English and return the form corresponding to the translation.
     */
    public String translateWord(String word, String fromLanguage, String toLanguage) {
        if (fromLanguage.equals("en"))
            return translateWordFromEnglish(word, toLanguage);

        if (toLanguage.equals("en"))
            return translateWordToEnglish(word, fromLanguage);

        Dictionar dictionary = getDictionaryHashMap().get(fromLanguage);
        for (Word wordCompare: dictionary.getWords()) {
            int index = checkPluralForm(word, wordCompare);
            if (index != INVALID_INDEX) {

                Dictionar dictionaryToTranslate = getDictionaryHashMap().get(toLanguage);
                for (Word wordTranslate : dictionaryToTranslate.getWords()) {
                    if (wordCompare.getWord_en().equals(wordTranslate.getWord_en())) {
                        return checkType(wordTranslate, index);
                    }
                }
            }
        }
        return word;
    }

    /**
     * Translate each word of a sentence and concatenate it to the final string.
     * If the first word of the sentence is translated, it is formatted to begin with capital letter.
     */
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
    public ArrayList<String> getSynonyms(String translateWord, String toLanguage)
                                        throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(toLanguage);
        ArrayList<String> listOfSynonyms = new ArrayList<>();
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

    /**
     * Returns the dictionary form of a word, regardless of the form in which it
     * is given as a parameter (singular, plural).
     */
    public String getDictionaryForm(String translatedWord, String toLanguage)
                                    throws NullPointerException {
        Dictionar dictionar = getDictionaryHashMap().get(toLanguage);
        for (Word word:dictionar.getWords()) {
            if (word.getWord().equals(translatedWord) || word.getPlural().contains(translatedWord) ||
                word.getSingular().contains(translatedWord)) {
                return word.getWord();
            }
        }
        return translatedWord;
    }

    /**
     * Returns the maximum number of synonyms of a word. In the hashmap received as a parameter,
     * each word is a key that has the value of a list of synonyms. If the maximum is greater
     * than 3, return 3.
     */
    public int getMaxSynonyms(HashMap<String, ArrayList<String>> synonyms) {
        int numberMaxSynonyms = 0;
        for (Map.Entry<String, ArrayList<String>> set : synonyms.entrySet()) {
            for (String str : set.getValue())
                numberMaxSynonyms++;
        }

        return Math.min(numberMaxSynonyms, MAX_SYNONYMS);
    }

    /**
     * Make a map in which the keys are the words in the sentence and the values are the lists of
     * synonyms translated into the language received as a parameter. Find the maximum number of
     * synonyms for a word. Synonymous sentences are formed and added to the list of sentences.
     * If a word has no synonyms, the main form is used in the translated sentence. If a word has
     * fewer synonyms, the sentences are always formed with its first synonym.
     */
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

    /**
    * The method is searching in the database for the dictionary corresponding to the language
     * * received as a parameter. Look up the word in the dictionary and add all
     * its definitions to a list. If the word is not found in the dictionary,
     * a corresponding message is displayed. The method returns the list of definitions sorted
     * in ascending order by the year the dictionaries appeared
    */
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
     * Exports a JSON dictionary, in which the words are sorted alphabetically and
     * the definitions are sorted in ascending order by the year of publication of each dictionary.
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
                Features.sortDefinitions(definitions);
            }
            word.put("definitions", definitions);
            words.add(word);
        }
        Features.sortWords(words);
        obj.put("Dictionary", words);
        Features.writeJson(obj);
    }

    /**
     * The database is updated with the information in the files and all
     * the dictionaries in it are displayed.
     */
    public static void main (String[] args) {
        try {
            Database database = Administration.readAllFiles(new File("init"));
            Features.printMap(database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}