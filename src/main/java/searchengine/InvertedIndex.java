package searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class creates an InvertedIndex object. To fill the index, call method
 * createIndex().
 * 
 * @author DIHJ, SULE, LINJ and SIKO
 * @version 1.0
 * @since 13/12 - 2020 
 * 
 */

public class InvertedIndex {
    List<Page> allPages;
    Map<String, ArrayList<Integer>> hashMap;
    ArrayList<Integer> integerList;
    Map<String, Integer> wordCountMap;


    /**
     * @param allPages The list of all pages, created by WebServer
     */
    public InvertedIndex(List<Page> allPages) {
        this.allPages = allPages;
        hashMap = new HashMap<>();
        wordCountMap = new HashMap<>();
    }

    /**
     * Creates the inverted index
     */
    public void createIndex() {
        int lineIndex = 0;
        int pageIndex = 0;

        for (Page page : allPages) {
            for (String string : page.getWords()) {
                if (!hashMap.containsKey(string)) {
                    wordCountMap.put(string, 1);
                    hashMap.put(string, new ArrayList<>());
                    hashMap.get(string).add(pageIndex);
                    lineIndex++;
                } else { 
                    if (!hashMap.get(string).contains(pageIndex)) {
                        Integer n = wordCountMap.get(string);
                        wordCountMap.put(string, n + 1);
                        hashMap.get(string).add(pageIndex);
                    }
                    lineIndex++;
                }
            }
            lineIndex = 0;
            pageIndex++;
        }
    }

    /**
     * Get the word count
     * @return wordCountMap
     */
    public Map<String, Integer> getWordCountMap(){
        return wordCountMap;
    }
}
