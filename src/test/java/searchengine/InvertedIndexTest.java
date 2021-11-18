package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)

public class InvertedIndexTest {
    private InvertedIndex index;
    List<Page> allPages;

    @BeforeAll
    public void setUp(){
        allPages = new ArrayList<>();
        index = new InvertedIndex(allPages);
        allPages.add(new Page("www.test1.com", "Title1", Arrays.asList("word1","word2","word3")));
        allPages.add(new Page("www.test2.com", "Title2", Arrays.asList("word1","word3","word4")));
        allPages.add(new Page("www.test3.com", "Title3", Arrays.asList("word2","word4","word5")));
    }
    
    @Test
    public void get_list_of_index_using_inverted_index(){
        index.createIndex();
        var key = "word1";
        assertEquals("[0, 1]", index.hashMap.get(key).toString());
    }

    @Test
    public void storing_same_key_using_inverted_index(){
        index.createIndex();
        assertEquals(5, index.hashMap.size());
    }
    
}