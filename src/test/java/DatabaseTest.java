import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class DatabaseTest {
    private Database database;

    @Before
    public void init() {
        try {
            database = Administration.readAllFiles(new File("init"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddWord() {
        File file1 = null, file2 = null, file3 = null;
        try {
            file1 = new File("tests\\addWord_tests\\test1.json");
            file2 = new File("tests\\addWord_tests\\test2.json");
            file3 = new File("tests\\addWord_tests\\test3.json");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Word word1 = Administration.readWordJson(file1);
            Word word2 = Administration.readWordJson(file2);
            Word word3 = Administration.readWordJson(file3);

            assert database != null;
            boolean result1 = database.addWord(word1, "ro");
            boolean result2 = database.addWord(word2, "ro");
            boolean result3 = database.addWord(word3, "ro");

            Assertions.assertTrue (result1);
            Assertions.assertTrue (result2);
            Assertions.assertTrue (result3);
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    @Test
    public void testDeleteWord() {
        try {
            boolean result1 = database.removeWord("chat", "fr");
            boolean result2 = database.removeWord("manger", "ro");

            Assertions.assertTrue(result1);
            Assertions.assertFalse(result2);
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        } 
    }

    @Test
    public void testAddDefinitionForWord() {
        try {
            File file1 = new File("tests\\addDefinitionForWord\\test1.json");
            Definition definition1 = Administration.readDefinitionJson(file1);
            boolean result1 =  database.addDefinitionForWord("chat", "fr", definition1);

            File file2 = new File("tests\\addDefinitionForWord\\test2.json");
            Definition definition2 = Administration.readDefinitionJson(file2);
            boolean result2 = database.addDefinitionForWord("pisică", "ro", definition2);

            Assertions.assertFalse(result1);
            Assertions.assertTrue(result2);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveDefinition() {
        try {
            boolean result1 =  database.removeDefinition("pisică", "ro", "Dicționar de sinonime");
            boolean result2 =  database.removeDefinition("pisică", "fr", "Dicționar universal al " +
                    "limbei române, ediția a VI-a)");
            Assertions.assertTrue(result1);
            Assertions.assertFalse(result2);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testTranslateWord() {
        assert database != null;
        String result1 = database.translateWord("merge", "ro", "fr");
        String result2 = database.translateWord("cat", "en", "fr");
        String result3 = database.translateWord("chat", "fr", "en");
        String result4 = database.translateWord("chat", "fr", "ro");

        Assertions.assertEquals("merge", result1);
        Assertions.assertEquals("chat", result2);
        Assertions.assertEquals("cat", result3);
        Assertions.assertEquals("pisică", result4);
    }

    @Test
    public void testTranslateSentences() {
        String sentenceTest1 = "Chats mangent";
        String result1 = database.translateSentence(sentenceTest1, "fr", "ro");

        String sentenceTest2 = "Pisică mănâncă";
        String result2 = database.translateSentence(sentenceTest2, "ro", "fr");

        String sentenceTest3 = "Pisică merge";
        String result3 = database.translateSentence(sentenceTest3, "ro", "fr");

        Assertions.assertEquals("Pisici mănâncă", result1);
        Assertions.assertEquals("Chat mange", result2);
        Assertions.assertEquals("Chat merge", result3);
    }

    @Test
    public void testArrayOfSynonyms() {
        try {
            String sentenceTest1 = "Chats mangent";
            ArrayList<String> synonyms = new ArrayList<String>();
            synonyms.add("Mâță mesteca ");
            synonyms.add("Cotoroabă inghiti ") ;
            synonyms.add("Cătușă hrani ");
            ArrayList<String> result1 = database.translateSentences(sentenceTest1, "fr", "ro");
            Assertions.assertEquals(synonyms, result1);

            String sentenceTest2 = "Pisică merge";
            ArrayList<String> synonyms2 = new ArrayList<String>();
            synonyms2.add("Greffier merge ");
            synonyms2.add("Mistigri merge ") ;
            synonyms2.add("Matou merge ");
            ArrayList<String> result2 = database.translateSentences(sentenceTest2, "ro", "fr");
            Assertions.assertEquals(synonyms2, result2);
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDefinitionsForWord() {
        String language1 = "ro", language2 = "ro";
        String word1 = "câine", word2 = "caine";
        Dictionar dictionar = database.getDictionaryHashMap().get(language1);
        ArrayList<Definition> definitions = new ArrayList<Definition>();
        for (Word wordCompare: dictionar.getWords()) {
            if (wordCompare.getWord().equals(word1)) {
                definitions.addAll(wordCompare.getDefinitions());
            }
        }
        definitions.sort(Comparator.comparing(Definition::getYear));
        try {
            ArrayList<Definition> result1 = database.getDefinitionsForWord(word1, language1);
            Assertions.assertEquals(definitions, result1);

            ArrayList<Definition> result2 = database.getDefinitionsForWord(word2, language2);
            Assertions.assertNull(result2);
        } catch (Exception e) {
            System.out.println("The dictionary does not exist");
            e.getMessage();
        }
    }

    @Test
    public void testExportJson() {
        try {
            database.exportDictionary("sp");
            database.exportDictionary("fr");
        } catch (Exception e) {
            System.out.println("The dictionary does not exist");
            e.getMessage();
        }
    }
}
