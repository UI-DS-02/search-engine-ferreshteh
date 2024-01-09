package search_engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import set.Set;


public class InvertedIndex {
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
