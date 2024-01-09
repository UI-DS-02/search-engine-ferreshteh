package search_engine;

import java.io.*;
import java.util.regex.Pattern;
public class Document {
    private String[] strings;
    private String[] names;

    public void setStrings(String[] strings) {
        this.strings = strings;
    }

    public String[] getNames() {
        return names;
    }

    public Document(File[] files, File[] out) throws IOException {

        if (files != null) {
            strings = new String[files.length];
            names = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                editDocuments(files[i], out[i]);
                names[i] = files[i].getName();
            }
            for (int i = 0; i < out.length; i++) {
                assert strings != null;
                strings[i] = readContent(out[i]);
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
        cleaned = Pattern.compile("[^a-zA-Z0-9\\s]").matcher(cleaned).replaceAll(" ");

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
