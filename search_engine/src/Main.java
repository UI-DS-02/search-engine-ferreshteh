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
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.addDocument(document);
        int input = 0;
        while (input != 3) {
            System.out.println("1- searching word");
            System.out.println("2- progressed searching ");
            Scanner sc = new Scanner(System.in);
            input = sc.nextInt();
            sc.nextLine();
            if (input == 1) {

                System.out.println("enter words");
                String function = sc.nextLine();

                String[] words = function.split(" ");
                String[] And = new String[words.length];
                String[] or = new String[words.length];
                String[] Not = new String[words.length];

                int index1 = 0;
                int index2 = 0;
                int index3 = 0;
                for (int i = 0; i < words.length; i++) {
                    if (words[i].charAt(0) == '+') {
                        words[i] = words[i].toLowerCase();
                        words[i] = words[i].replace("+", "");
                        or[index2] = words[i];
                        index2++;
                    } else if (words[i].charAt(0) == '-') {
                        words[i] = words[i].toLowerCase();
                        words[i] = words[i].replace("-", "");
                        Not[index3] = words[i];
                        index3++;
                    } else if (Character.isLetter(words[i].charAt(0))) {
                        words[i] = words[i].toLowerCase();
                        And[index1] = words[i];
                        index1++;
                    }
                }

                //-------------------------------------------------------

                Set<String> set1 = invertedIndex.search_1(And);

                Set<String> set2 = invertedIndex.search_2(or);

                Set<String> set3 = invertedIndex.search_3(Not);
                //-----------------------------------------------------------
               if(set1.size()>=1 && set2.size()==0 && set3.size()==0){
                   for (String set:set1){
                       System.out.println(set);
                   }
               }
               else if(set1.size()>=1&&set2.size()>=1&&set3.size()>=1){
                   set1=invertedIndex.And(set1,set2);
                   set1=invertedIndex.Not(set1,set3);
                   for (String set:set1){
                       System.out.println(set);
                   }
               }
               else if(set2.size()>=1 && set1.size()==0 && set3.size()==0){
                   for (String set:set2){
                       System.out.println(set);
                   }
               }
               else if(set3.size()>=1 && set1.size()==0 && set2.size()==0){
                   for (String set:set3){
                       System.out.println(set);
                   }
               }
               else if(set1.size()>=1&& set2.size()>=1){
                   set1=invertedIndex.And(set1,set2);
                   for (String set:set1){
                       System.out.println(set);
                   }
               }
               else if(set1.size()>=1&&set3.size()>=1){
                   set1=invertedIndex.Not(set1,set3);
                   for (String set:set1){
                       System.out.println(set);
                   }

               }
               else if(set2.size()>=1&&set3.size()>=1){
                   set2=invertedIndex.Not(set2,set3);
                   for (String set:set2){
                       System.out.println(set);
                   }

               }
                set1=null;
                set2=null;
                set3=null;

            } else if (input == 2) {
                Set <String>set=new Set<String>();
                String function=sc.nextLine();
                set=invertedIndex.new_search(function);
                for(String sets:set){
                    System.out.println(sets);
                }
            }
        }
    }
}


class Document {
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

class InvertedIndex {
    private boolean new_search = false;

    public boolean isNew_search() {
        return new_search;
    }

    private Map<String, Set<String>> index;

    public InvertedIndex() {
        index = new HashMap<>();
    }

    public void addDocument(Document document) {
        String[] words = document.getStrings();
        String[] names = document.getNames();
        for (int i = 0; i < words.length; i++) {
            {
                String[] word = split(words[i]);
                for (int j = 0; j < word.length; j++) {
                    if (!index.containsKey(word[j])) {
                        Set<String> set = new Set<String>();
                        set.add(names[i]);
                        index.put(word[j], set);
                    } else {
                        index.get(word[j]).add(names[i]);
                    }
                }
            }
        }
    }

    public String[] split(String str) {
        String[] result = str.split(" ");
        return result;
    }

    public Set<String> search_1(String[] query) {
        Set<String> result = new Set<>();
        for (int i = 0; i < query.length; i++) {
            if (i == 0) {
                if (index.containsKey(query[i])) {
                    result.setVisit(true);
                    result = index.get(query[i]);
                }
            } else if (index.containsKey(query[i])) {
                Set<String> result1 = index.get(query[i]);
                result = And(result, result1);
                //result.add(index.get(query[i]).toString());
            }
        }
        return result;

    }

    public Set<String> And(Set<String> result, Set<String> set2) {
        ArrayList<String> array1 = result.getSet();
        ArrayList<String> array2 = set2.getSet();
        array1.retainAll(array2);
        return result;
    }


    public Set<String> search_2(String[] query) {
        Set<String> resultSet = new Set<>();
        for (int i = 0; i < query.length; i++) {
            if (i == 0) {
                if (index.containsKey(query[i])) {
                    resultSet = index.get(query[i]);
                }
            } else if (index.containsKey(query[i])) {
                Set<String> set2 = index.get(query[i]);
                Or(resultSet, set2);
            }
        }
        return resultSet;
    }

    public Set<String> Or(Set<String> result, Set<String> set2) {
        ArrayList<String> array1 = result.getSet();
        array1.removeIf(set2::contains);
        array1.addAll(set2.getSet());
        return result;
    }

    public Set<String> search_3(String[] query) {
        Set<String> result = new Set<>();
        for (int i = 0; i < query.length; i++) {
            if (i == 0) {
                if (index.containsKey(query[i])) {
                    result = index.get(query[i]);
                }
            } else if (index.containsKey(query[i])) {
                Set<String> set2 = index.get(query[i]);
                result = And(result, set2);
                result.add(index.get(query[i]).toString());
            }
        }
        return result;
    }

    public Set<String> Not(Set<String> result, Set<String> set2) {
        ArrayList<String> result_Arr = result.getSet();
        result_Arr.removeIf(set2::contains);
        return result;
    }


    public Set<String> new_search(String query) {
        Set<String> result = new Set<>();
        if (index.containsKey(query)) {
            result.add(index.get(query).toString());
        }
        if (result.size() == 0) {
            String[] keys = index.keySet().toArray(new String[0]);
            StringBuilder bestMatch = getClosestWord(query, keys);
            String[] new_keys = String.valueOf(bestMatch).split(" ");
            if (new_keys.length >= 1) {
                new_search = true;
            }
            for (int i = 0; i < new_keys.length; i++) {
                query = new_keys[i];

                if (i == 0) {
                    if (index.containsKey(query)) {
                        result = index.get(query);
                        result.add(new_keys[i]);
                        result.add("    ");
                    }
                } else if (index.containsKey(query)) {
                    Set<String> result1 = index.get(query);
                    result = Or(result, result1);
                    result.add(index.get(query).toString());
                    result.add(new_keys[i]);
                    result.add("    ");
                }
            }
        }
        return result;
    }

    //--------------------------
    public StringBuilder getClosestWord(String word, String[] dictionary) {
        StringBuilder closestWord = new StringBuilder();
        boolean same;
        for (String dicWord : dictionary) {
            same = distance(word, dicWord);
            if (same) {
                closestWord.append(dicWord).append(" ");
            }
        }
        return closestWord;
    }

    public boolean distance(String main, String s2) {
        int len1 = main.length();
        int len2 = s2.length();

        int i, cost = 0;
        if (Math.abs(len1 - len2) >= 2)
            return false;
        int lenght = Math.min(len1, len2);
        String bigger_word;
        String smaller_word;
        if (len2 >= len1) {
            bigger_word = s2;
            smaller_word = main;
        } else {
            bigger_word = main;
            smaller_word = s2;
        }
        for (i = 0; i < lenght; i++) {
            char s = smaller_word.charAt(i);
            char b = bigger_word.charAt(i);
            if (s != b) {
                cost++;
            }
        }
        cost += Math.max(len2, len1) - i;
        if (cost <= 1)
            return true;
        return false;
    }

}


class Set<T> implements Iterable<T> {
    private boolean visit = false;

    public boolean isVisit() {
        return visit;
    }

    public void setVisit(boolean visit) {
        this.visit = visit;
    }

    private ArrayList<T> set;

    public Set() {
        set = new ArrayList<>();
    }

    public boolean add(T item) {
        if (!set.contains(item)) {
            set.add(item);
            return true;
        }
        return false;
    }

    public boolean remove(T item) {
        return set.remove(item);
    }

    public boolean contains(T item) {
        return set.contains(item);
    }

    public int size() {
        return set.size();
    }

    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set<?> set1 = (Set<?>) o;
        return set.equals(set1.set);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            result.append(set.get(i).toString()).append(" ");
        }
        return result.toString();
    }

    public ArrayList<T> getSet() {
        return set;
    }

    public void setSet(ArrayList<T> set) {
        this.set = set;
    }
}
//--------------------------------

