package searchengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
public class FileLoaderTest {
    private FileLoader loader;
    
    
    @BeforeAll
    public void setUp(){

    }

    @Test
    public void read_proper_textfile(){
        loader = new FileLoader("data/test-file.txt");
        var pages = loader.allPages;
        assertEquals(2, pages.size());

        assertEquals("title1", pages.get(0).getTitle());
        assertEquals("title2", pages.get(1).getTitle());

        assertEquals("word1", pages.get(0).getWords().get(0));
        assertEquals("word2", pages.get(0).getWords().get(1));
        assertEquals("word1", pages.get(1).getWords().get(0));
        assertEquals("word3", pages.get(1).getWords().get(1));

        assertEquals("http://page1.com", pages.get(0).getUrl());
        assertEquals("http://page2.com", pages.get(1).getUrl());
    }
    
    @Test
    public void read_wrong_textfile_with_error_number_of_pages(){
        loader = new FileLoader("data/test-file-errors.txt");
        var pages = loader.allPages;
        assertEquals(2, pages.size());
    }

    @Test
    public void read_wrong_textfile_title(){
        loader = new FileLoader("data/test-file-errors.txt");
        var pages = loader.allPages;

        assertEquals("title1", pages.get(0).getTitle());
        assertEquals("title2", pages.get(1).getTitle());
    }
    
    @Test
    public void read_wrong_textfile_word(){
        loader = new FileLoader("data/test-file-errors.txt");
        var pages = loader.allPages;

        assertEquals("word1", pages.get(0).getWords().get(0));
        assertEquals("word2", pages.get(0).getWords().get(1));
        assertEquals("word1", pages.get(1).getWords().get(0));
        assertEquals("word3", pages.get(1).getWords().get(1));
    }

    @Test
    public void read_wrong_textfile_url(){
        loader = new FileLoader("data/test-file-errors.txt");
        var pages = loader.allPages;

        assertEquals("http://page1.com", pages.get(0).getUrl());
        assertEquals("http://page2.com", pages.get(1).getUrl());
    }

    
}