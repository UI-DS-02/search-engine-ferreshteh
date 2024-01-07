import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Document {
    private String fileName;
    private String content;

    public Document(File file) {
        this.fileName = file.getName();
        this.content = readContent(file);
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

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }
}

class InvertedIndex {
    private Map<String, Set<String>> index;

    public InvertedIndex() {
        index = new HashMap<>();
    }

    public void addDocument(Document document) {
        String[] words = document.getContent().split("\\s+");
        for (String word : words) {
            if (!index.containsKey(word)) {
                index.put(word, new HashSet<>());
            }
            index.get(word).add(document.getFileName());
        }
    }

    public void createInvertedIndex() {
        // The index has already been created while adding documents
    }

    public Set<String> search(String query) {
        if (index.containsKey(query)) {
            return index.get(query);
        } else {
            return new HashSet<>();
        }
    }
    private static void editDocuments(File inputFile, File outputFile) throws IOException {
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

    private static String removePunctuationAndSpaces(String text) {
        String regex = "\\P{L}+";
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(text);
        String result = ((Matcher) matcher).replaceAll(" ");
        return result.trim();
    }
}