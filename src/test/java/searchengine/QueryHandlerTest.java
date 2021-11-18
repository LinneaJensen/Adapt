package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


@TestInstance(Lifecycle.PER_CLASS)
public class QueryHandlerTest {
    private QueryHandler handler;
    private InvertedIndex lookupTable;
    Map<String, ArrayList<Integer>> invertedIndex;
    Response response;
    List<Page> allPages;
    Page page;
    List<Integer> integerResponse;
    String[] searchTerms;


    @BeforeAll
    public void setUp(){
        // pageRanker = new PageRanker();
        allPages = new ArrayList<>();
        lookupTable = new InvertedIndex(allPages);
        lookupTable.createIndex();
        invertedIndex = lookupTable.hashMap;
        response = new Response();
        searchTerms = new String[0];
        integerResponse = invertedIndex.get(searchTerms);
    
        handler = new QueryHandler("");
        allPages.add(new Page("test1.com", "Title1", Arrays.asList("word0")));
        allPages.add(new Page("test2.com", "Title2", Arrays.asList("word0","word1","word1","word3")));
        allPages.add(new Page("test3.com", "Title3", Arrays.asList("word2","word4","word5")));
        allPages.add(new Page("test4.com", "Title4", Arrays.asList("word0","word3","word5")));
        allPages.add(new Page("test5.com", "Title5", Arrays.asList("word0","word1","word4","word3","word5")));
        allPages.add(new Page("test6.com", "Title6", Arrays.asList("word2","word4")));

    }

    @Test
    public void single_term(){
        assertEquals(2, handler.getMatchingWebpages("word1").size());
        assertEquals(3, handler.getMatchingWebpages("word5").size());
        assertEquals(3, handler.getMatchingWebpages("word4").size());
        assertEquals(2, handler.getMatchingWebpages("word2").size());
        assertEquals(2, handler.getMatchingWebpages("word9").size()); 
    }


    @Test
    public void multiple_term(){
        assertEquals(2, handler.getMatchingWebpages("word1 word3").size());
        assertEquals(1, handler.getMatchingWebpages("word5 word4 word1").size());
        assertEquals(2, handler.getMatchingWebpages("word4 word2").size());
        assertEquals(3, handler.getMatchingWebpages("word0 word3").size());
        
    }

    @Test
    public void term_with_OR(){
        assertEquals(3, handler.getMatchingWebpages("word1 OR word3").size());
        assertEquals(1, handler.getMatchingWebpages("word5 OR word4 OR word1").size());
        assertEquals(3, handler.getMatchingWebpages("word4 ORword2").size());
        assertEquals(3, handler.getMatchingWebpages("ORword0 word3").size());
        assertEquals(4, handler.getMatchingWebpages("word0 word3OR").size());
        assertEquals(3, handler.getMatchingWebpages("word9 OR word3").size());
        assertEquals(3, handler.getMatchingWebpages("word0 word9 OR word3").size());
    }

    @Test
    public void cornercases(){
        assertEquals(0, handler.getMatchingWebpages(" ").size());
        assertEquals(0, handler.getMatchingWebpages("?").size());
        assertEquals(0, handler.getMatchingWebpages("OR").size());
        assertEquals(0, handler.getMatchingWebpages("CATORDOG").size());
       
    }
}

