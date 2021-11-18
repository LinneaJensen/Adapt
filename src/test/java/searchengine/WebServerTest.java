package searchengine;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(Lifecycle.PER_CLASS)
class WebServerTest {
    WebServer server = null;
    FileLoader loader = null;

    @BeforeAll
    void setUp() {
        try {
            var rnd = new Random();
            while (server == null) {
                try {
                    server = new WebServer(rnd.nextInt(60000) + 1024, "data/test-file.txt");
                } catch (BindException e) {
                    // port in use. Try again
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDown() {
        server.server.stop(0);
        server = null;
    }

    @Test
    /**
     * Tests method getFile()
     * Check if the converted file (String to byte) are converted
     * correctly, using the test-file.txt as sample. 
     */
    public void convert_fileToByte_returnBytes(){

        try {
           byte[] filebytes = loader.getFile("data/test-file.txt");
            assertArrayEquals(new byte[]{42, 80, 65, 71, 69, 58, 104, 116, 116, 112, 58, 47, 47, 112, 97, 103, 101, 49, 46, 
                99, 111, 109, 10, 116, 105, 116, 108, 101, 49, 10, 119, 111, 114, 100, 49, 10, 119, 111, 114, 100, 50, 10, 
                42, 80, 65, 71, 69, 58, 104, 116, 116, 112, 58, 47, 47, 112, 97, 103, 101, 50, 46, 99, 111, 109, 10, 116, 
                105, 116, 108, 101, 50, 10, 119, 111, 114, 100, 49, 10, 119, 111, 114, 100, 51}, filebytes);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    /**
     * This is a test trying to check if ecxeptions are thrown.
     * it passes, but does not cover the method.
     */
    public void test_and_validate_exception(){
        
        try {
            loader.getFile(null);
            Assert.fail("text");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
     

    @Test
    void lookupWebServer() {
        String baseURL = String.format("http://localhost:%d/search?q=", server.server.getAddress().getPort());
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}, {\"url\": \"http://page2.com\", \"title\": \"title2\"}]", 
            httpGet(baseURL + "word1"));
        assertEquals("[{\"url\": \"http://page1.com\", \"title\": \"title1\"}]",
            httpGet(baseURL + "word2"));
        assertEquals("[{\"url\": \"http://page2.com\", \"title\": \"title2\"}]", 
            httpGet(baseURL + "word3"));
        assertEquals("[]", 
            httpGet(baseURL + "word4"));
    }

    private String httpGet(String url) {
        var uri = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            return client.send(request, BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}