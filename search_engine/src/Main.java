import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        File inputDirectory = new File("C:\\Users\\NA\\sakhtemanDade\\gitHub\\search-engine-ferreshteh\\search_engine\\EnglishData");
        File out = new File("C:\\Users\\NA\\sakhtemanDade\\gitHub\\search-engine-ferreshteh\\search_engine\\EnglishData2");
        File[] files = inputDirectory.listFiles();
        File[] outs = out.listFiles();
        Document document = new Document(files, outs);
        InvertedIndex invertedIndex=new InvertedIndex();
        invertedIndex.addDocument(document);
    }
}

class Document {
    private String[] strings;
    public Document(File[] files, File[] out) throws IOException {

        if (files != null) {
            strings=new String[files.length];
            for (int i = 0; i < files.length; i++) {
                editDocuments(files[i], out[i]);
            }
            for(int i=0;i<out.length;i++){
                assert strings != null;
                strings[i]= readContent(out[i]);
            }
        }
    }
    public String[] getStrings() {
        return strings;
    }
    void editDocuments(File inputFile, File outputFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String editedLine = removePunctuationAndSpaces(line);
                writer.write(editedLine);
                writer.newLine();
            }
        }
    }
    String removePunctuationAndSpaces(String input) {
        String cleaned = input.toLowerCase();
        cleaned = Pattern.compile("[^a-zA-Z0-9\\s]").matcher(cleaned).replaceAll("");

        // Split the words and remove extra spaces
        cleaned = cleaned.trim().replaceAll("\\s+", " ");

        return cleaned;
    }
    private String readContent(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}

class InvertedIndex {
    private Map<String, Set<String>> index;

    public InvertedIndex() {
        index = new HashMap<>();
    }

    public void addDocument(Document document) {
        String[] words = document.getStrings();
        for (String word : words) {
            if (!index.containsKey(word)) {
                index.put(word, new HashSet<>());
            }
           // index.get(word).add(document.getStrings());
        }
    }

    public Set<String> search(String query) {
        if (index.containsKey(query)) {
            return index.get(query);
        } else {
            return new HashSet<>();
        }
    }

}
