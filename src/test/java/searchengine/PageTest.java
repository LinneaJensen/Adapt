package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
public class PageTest {
   
    @Test
    public void url_check() {
        List<String> words_list_checker = new ArrayList<>();
        words_list_checker.add("word1");
        Page page = new Page("url_checker", "title_checker", words_list_checker);
        assertEquals("url_checker", page.getUrl());
    }

    @Test
    public void title_check() {
        List<String> words_list_checker = new ArrayList<>();
        words_list_checker.add("word1");
        Page page = new Page("url_checker", "title_checker", words_list_checker);
        assertEquals("title_checker", page.getTitle());
    }

    @Test
    public void words_list_check() {
        List<String> words_list_checker = new ArrayList<>();
        words_list_checker.add("word1");
        Page page = new Page("url_checker", "title_checker", words_list_checker);
        assertEquals("word1", page.getWords().get(0));
    }
    
}