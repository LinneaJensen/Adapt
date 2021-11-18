package searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Handles a complex query and searches through an invertedIndex
 * and creates a result of response pages.
 * 
 * @author DIHJ, SULE, LINJ and SIKO
 * @version 1.0
 * @since 13/12 - 2020 A class that handles a query
 */

public class QueryHandler {
  Response response;
  private List<Page> allPages;
  private InvertedIndex lookupTable;
  Map<String, ArrayList<Integer>> invertedIndex;
  private FileLoader loader;
  PageRanker ranker;
  private Map<String, Integer> wordCountMap;
  ArrayList<Page> resultByOR;
  List<Integer> integerResponse;
  String[] searchTerms;

  /**
   * @param filename name of the file used in the search engine
   */
  public QueryHandler(String filename) {
    loader = new FileLoader(filename);
    response = new Response();
    allPages = loader.getPages();
    lookupTable = new InvertedIndex(allPages);
    lookupTable.createIndex();
    invertedIndex = lookupTable.hashMap;
    wordCountMap = lookupTable.wordCountMap;
    resultByOR = new ArrayList<>();
    integerResponse = new ArrayList<>();
    searchTerms = new String[0];
    ranker = new PageRanker(allPages, invertedIndex);
  }

  /**
   * Get the webpages that matches the queryString
   * @param queryString String of queries
   * @return responsePages a list of the pageresults converted to responseheaders
   */
  List<String> getMatchingWebpages(String queryString) {
    List<String> responsePages = new ArrayList<>();
    List<Page> pagesResponse = new ArrayList<>();
    HashSet<Page> duplicateRemover = new HashSet<>();
    queryString = queryString.toLowerCase();
    if (queryString.contains("%20or%20")) {
      pagesResponse = getBySplitOR(queryString);
      duplicateRemover.addAll(pagesResponse);
      pagesResponse.clear();
      pagesResponse.addAll(duplicateRemover);
      duplicateRemover.clear();
    } else {
      if (queryString.contains("%20")) {
        searchTerms = queryString.toLowerCase().split("%20");
        integerResponse = getIntersection(searchTerms);
        pagesResponse = ranker.scoreAssigning(false, true, integerResponse, this.searchTerms, wordCountMap);
      } else {
        if (invertedIndex.containsKey(queryString)) {
          searchTerms = queryString.split(" ");
          integerResponse = invertedIndex.get(queryString);
          pagesResponse = ranker.scoreAssigning(false, true, integerResponse, this.searchTerms, wordCountMap);
        } else {
          integerResponse = null;
        }

      }
    }
    try {
      for (Page page : pagesResponse) {
        responsePages.add(page.getFormattedString());
      }
    } catch (NullPointerException e) {
      
    }
    return responsePages;
  }

  /**
   * Get the intersection result when using a searchTerm to search through the inverted index
   * @param searchTerms an array of words extracted from the query
   * @return localVariable a list of the result pages index number
   */
  public ArrayList<Integer> getIntersection(String[] searchTerms) {
    ArrayList<Integer> localVariable = new ArrayList<>();
    boolean firstWord = true;
    for (String word : searchTerms) {
      if (firstWord) {
        if (invertedIndex.containsKey(word)) {
          localVariable.addAll(invertedIndex.get(word));
          firstWord = false;
        } else {
          localVariable = null;
          firstWord = false;
        }
      } else {
        ArrayList<Integer> compareList = new ArrayList<>();
        compareList = invertedIndex.get(word);
        if (compareList == null) {
          localVariable = null;
        } else {
          try {
            localVariable.retainAll(compareList);
          } catch (NullPointerException e) {

          }
        }
      }
    }
    return localVariable;
  }
  
  /**
   * Get the result list of pages after sanitizing a complex query splitted by OR
   * @param queryString String of queries
   * @return resultByOR Pages that match the queries
   */
  public List<Page> getBySplitOR(String queryString) {
    resultByOR = new ArrayList<>();
    ArrayList<Integer> localVariableOR = new ArrayList<>();
    List<Page> pagesResponse = new ArrayList<>();
    var queries = queryString.split("%20or%20");
    for (String query : queries) {
      searchTerms = query.toLowerCase().split("%20");
      localVariableOR = getIntersection(searchTerms);
      pagesResponse = ranker.scoreAssigning(false, true, localVariableOR, this.searchTerms, wordCountMap);
      try {
        resultByOR.addAll(pagesResponse);
      } catch (NullPointerException e) {
        
      }
      
    }
    ranker.sorting(resultByOR);
    return resultByOR;
  }
}
