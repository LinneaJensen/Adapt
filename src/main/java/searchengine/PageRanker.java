package searchengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A class responsible for assigning all pages that will be shown in the search engine
 * a score, which they are subsequently sorted by.
 * 
 * 
 * @author DIHJ, SULE, LINJ and SIKO
 * @version 1.0
 * @since 13/12 - 2020 
 * 
 */

public class PageRanker {

  private List<Page> allPages;
  Map<String, ArrayList<Integer>> invertedIndex;

  /**
   * 
   * @param allPages a list of all the pages used in the searchengine
   * @param invertedIndex the invertedindex map
   */
  public PageRanker(List<Page> allPages, Map<String, ArrayList<Integer>> invertedIndex) {

    this.allPages = allPages;
    this.invertedIndex = invertedIndex;
  }

  /**
   * Assigning a score to each page in the list of result pages. 
   * @param rankWithTF if true, TF is used to rank
   * @param rankWithTFIDF if true, TFIDF is used to rank
   * @param integerResponse a list of Integer that is the result from the search
   * @param searchTerms the array of terms used for the score assigning
   * @param wordCountMap a map with the searchterm as a key and the number of pages it appears in as a value
   * @return pagesResponse a list of Page objects with rankings
   */
  public List<Page> scoreAssigning(boolean rankWithTF, boolean rankWithTFIDF, List<Integer> integerResponse,
      String[] searchTerms, Map<String, Integer> wordCountMap) {
    rankWithTF = false;
    rankWithTFIDF = true;
    List<Page> pagesResponse = new ArrayList<>();

    try {
      if (integerResponse.size() > 0) {
        for (Integer index : integerResponse) {
          pagesResponse.add(allPages.get(index));
        }
      }
    } catch (NullPointerException e) {
      pagesResponse = null;
    }

    try {
      for (Page page : pagesResponse) {
        double tempTfScore = 0.0;
        double tempIdfScore = 0.0;

        for (String entry : searchTerms) {
          if (rankWithTFIDF) {
            try {
              tempTfScore += page.getTfMap().get(entry);
              tempIdfScore += Math.log(allPages.size() / wordCountMap.get(entry));
            } catch (Exception e) {
            }
          } else if (rankWithTF) {
            try {
              tempTfScore += page.getTfMap().get(entry);
            } catch (Exception e) {
            }
          }
        }
        if (tempTfScore > page.getTfScore()) {
          page.setTFScore(tempTfScore);
        }
        if (tempIdfScore > page.getIdfScore()) {
          page.setIDFScore(tempIdfScore); 
        }
      }
      sorting(pagesResponse);
    } catch (NullPointerException e) {

    }

    return pagesResponse;
  }

  /** Sorting based on tfIdf. Can easily be changed. 
   * @param pagesResponse a list of Page objects with rankings
   */
  public void sorting(List<Page> pagesResponse) {
    Collections.sort(pagesResponse, 
    Comparator.comparing(Page::getTfIdfScore)
    .thenComparing(Page::getTitle));
  }
}