import search_engine.Document;
import search_engine.InvertedIndex;

import java.util.*;
import java.io.File;
import java.io.IOException;

import search_engine.StopWordRemoval;
import set.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        File inputDirectory = new File("C:\\Users\\NA\\sakhtemanDade\\gitHub\\search-engine-ferreshteh\\search_engine\\EnglishData");
        File out = new File("C:\\Users\\NA\\sakhtemanDade\\gitHub\\search-engine-ferreshteh\\search_engine\\EnglishData2");
        File[] files = inputDirectory.listFiles();
        File[] outs = out.listFiles();
        Document document = new Document(files, outs);

        //----------------------------------
        //---------------------------------
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


                StopWordRemoval stop = new StopWordRemoval();
                String[] words = function.split(" ");
                words = stop.removeStopWord(words);


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
                if (set1.size() >= 1 && set2.size() == 0 && set3.size() == 0) {
                    for (String set : set1) {
                        System.out.println(set);
                    }
                } else if (set1.size() >= 1 && set2.size() >= 1 && set3.size() >= 1) {
                    set1 = invertedIndex.And(set1, set2);
                    set1 = invertedIndex.Not(set1, set3);
                    for (String set : set1) {
                        System.out.println(set);
                    }
                } else if (set2.size() >= 1 && set1.size() == 0 && set3.size() == 0) {
                    for (String set : set2) {
                        System.out.println(set);
                    }
                } else if (set3.size() >= 1 && set1.size() == 0 && set2.size() == 0) {
                    for (String set : set3) {
                        System.out.println(set);
                    }
                } else if (set1.size() >= 1 && set2.size() >= 1) {
                    set1 = invertedIndex.And(set1, set2);
                    for (String set : set1) {
                        System.out.println(set);
                    }
                } else if (set1.size() >= 1 && set3.size() >= 1) {
                    set1 = invertedIndex.Not(set1, set3);
                    for (String set : set1) {
                        System.out.println(set);
                    }

                } else if (set2.size() >= 1 && set3.size() >= 1) {
                    set2 = invertedIndex.Not(set2, set3);
                    for (String set : set2) {
                        System.out.println(set);
                    }

                }
                set1 = null;
                set2 = null;
                set3 = null;

            } else if (input == 2) {
                Set<String> set = new Set<String>();
                String function = sc.nextLine();
                set = invertedIndex.new_search(function);
                for (String sets : set) {
                    System.out.println(sets);
                }
            }
        }
    }
}

//--------------------------------

