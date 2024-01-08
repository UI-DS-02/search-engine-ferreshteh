import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();

        // Adding documents to the index
        index.addDocument("text1", "java programming language");
        index.addDocument("text2", "c++ programming language");
        index.addDocument("text3", "python programming language");

        // Performing the search
        String query = "javac";
        List<String> results = index.search(query);

        // Printing the search results
        System.out.println("Search results for \"" + query + "\":");
        for (String result : results) {
        System.out.println(result);
        }
        }}

     class InvertedIndex {
        private Map<String, Set<String>> index;

        public InvertedIndex() {
            index = new HashMap<>();
        }

        public Set<String> search_1(String[] query) {
            Set<String> result=new Set<>();
            for (int i = 0; i < query.length; i++) {
                if(i==0){
                    if(index.containsKey(query[i])){
                        result.setVisit(true);
                    }
                }
               else if (index.containsKey(query[i]) && !result.isVisit()) {
                    result.add(query[i]);
                }
            }
            return result;
        }

        public Set<String> search_2(String[] query) {
                for (int i = 0; i < query.length; i++) {
                    if (index.containsKey(query[i])) {
                        i++;
                    } else {

                    }
                }
                return resultSet.size();
            }
        }
}

