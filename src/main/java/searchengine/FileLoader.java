package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * A class that reads in the file and adds the text as pages on a list of all pages. 
 * 
 * @author DIHJ, SULE, LINJ and SIKO
 * @version 1.0
 * @since 13/12 - 2020 
 * 
 */

public class FileLoader {

  String filename;
  ArrayList<Page> allPages;

  /**
   * @param filename name of the file to be used in the search engine.
   */
  public FileLoader(String filename) {

    try {
      allPages = new ArrayList<>();
      readFile(filename);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (ConcurrentModificationException e) {
      e.printStackTrace();
    }
  }

  /**
   * reads the file, devides it to pages and then adds them to a list of all pages
   * @param filename name of the file to be used in the search engine.
   * @throws IOException if the input is empty
   */
  private void readFile(String filename) throws IOException {
    try {
      List<String> lines = Files.readAllLines(Paths.get(filename));
      var lastIndex = lines.size();
      for (var i = lines.size() - 1; i >= 0; --i) {
        if (lines.get(i).startsWith("*PAGE")) {
          var url = lines.get(i).substring(6);
          var title = lines.get(i + 1);
          var words = lines.subList(i + 2, lastIndex);
          if (url != null && title != null && words.size() >= 1) {
            Page page = new Page(url, title, words);
            allPages.add(page);
          } else {
            i -= 1;
          }

          lastIndex = i;
        } else {
          
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Collections.reverse(allPages);
  }

  //Retrieves a file from the path
  /**
   * Get the file from the filepath given in parameter
   * @param filename filepath for file
   */
  byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }

  /**
   * Get all pages
   * @return allPages
   */
  public ArrayList<Page> getPages() {
    return allPages;
  }

}
