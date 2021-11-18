package searchengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * A class that creates a Page opbject from the list of pages and defines a score for every page
 * 
 * @author DIHJ, SULE, LINJ and SIKO
 * @version 1.0
 * @since 13/12 - 2020 
 * 
 */

public class Page {
    private String url;
    private String title;
    private List<String> words;
    private HashMap<String, Double> tfMap;
    protected double idfScore;
    protected double tfScore;

    /**
     * Creates a object of type Page
     * 
     * @param url the url that page contains
     * @param title the title of the page
     * @param words all the words in the page  
     */
    public Page(String url, String title, List<String> words) {
        this.url = url;
        this.title = title;
        this.words = words;
        tfMap = new HashMap<>();
        idfScore = 0.0;
        tfScore = 0.0;
        calculateTf();
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getWords() {
        return words;
    }

    /**
     * Get the content of the page without the url
     * @return all the content
     */
    public List<String> getContent() {
        List<String> content = new ArrayList<>();
        content.add(getTitle());
        content.addAll(getWords());

        return content;

    }

    /**
     * Get the formation needed for the response header
     * @return formattedString
     */
    public String getFormattedString() {
        var formattedString = "";
        formattedString = String.format("{\"url\": \"%s\", \"title\": \"%s\"}", getUrl(), getTitle());
        return formattedString;
    }


    /**
     * calculates the term frequency that each page holds for each word.
     */
    public void calculateTf() {
        for (String word : getContent()) {
            if (!tfMap.containsKey(word)) {
                tfMap.put(word, 1.0);
            } else {
                double n = tfMap.get(word);
                tfMap.put(word, n + 1);
            }
        }
    }

    /**
     * Get TfMap
     * @return tfMap
     */
    public Map<String, Double> getTfMap() {
        return tfMap;
    }

    /**
     * Sets TF score
     * @param tfScore TF Score
     */
    public void setTFScore(double tfScore) {
        this.tfScore = tfScore;
    }

    /**
     * Set IDF score
     * @param idfScore IDF score
     */
    public void setIDFScore(double idfScore) {
        this.idfScore = idfScore;
    }

    /**
     * Get TFScore
     * @return TF score
     * 
     */
    public double getTfScore() {
        return tfScore;
    }

    /**
     * Get IdfScore
     * @return IDF score
     */
    public double getIdfScore() {
        return idfScore;
    }

    /**
     * Get TFIdfScore
     * @return The product TF score and IDF score
     */
    public double getTfIdfScore() {
        return tfScore * idfScore;
    }


}
