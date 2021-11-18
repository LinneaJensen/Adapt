package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

/**
 * A class that handles all the information retrieved from and sent to 
 * the webserver in order to run the search engine. 
 * 
 * @author DIHJ, SULE, LINJ and SIKO
 * @version 1.0
 * @since 13/12 - 2020 
 * */


public class WebServer {

  static final int PORT = 8080;
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;
  private FileLoader loader;
  QueryHandler handler;
  HttpServer server;
  Response response;

  /**
   * @param port port number location for the webserver
   * @param filename name of the file to be used in the search engine.
   */
  WebServer(int port, String filename) throws IOException {
    loader = new FileLoader(filename);
    response = new Response();

    try {
      handler = new QueryHandler(filename);
      initiateServer(port);
      server.start();
      printServerMessage(port);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Starts the server
   * @param port port number location for the webserver
   * @throws IOException
   */
  private void initiateServer(int port) throws IOException {
    server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
    server.createContext("/", io -> response.respond(io, 200, "text/html", loader.getFile("web/index.html")));
    server.createContext("/search", io -> search(io));
    server.createContext("/favicon.ico",
        io -> response.respond(io, 200, "image/x-icon", loader.getFile("web/favicon.ico")));
    server.createContext("/code.js",
        io -> response.respond(io, 200, "application/javascript", loader.getFile("web/code.js")));
    server.createContext("/style.css", io -> response.respond(io, 200, "text/css", loader.getFile("web/style.css")));

  }

  /**
   * Prints user message in terminal
   * @param port port number location for the webserver
   */
  private void printServerMessage(int port) {
    String msg = " WebServer running on http://localhost:" + port + " ";
    System.out.println("╭" + "─".repeat(msg.length()) + "╮");
    System.out.println("│" + msg + "│");
    System.out.println("╰" + "─".repeat(msg.length()) + "╯");
  }

  /** gets the query, searches through the index and sends a response to the webserver
   * @param io input from HttpExchange
   */
  void search(HttpExchange io) {
    String queryString =  io.getRequestURI().getRawQuery().split("=")[1];
    var bytes = handler.getMatchingWebpages(queryString).toString().getBytes(CHARSET);
    response.respond(io, 200, "application/json", bytes); // Send list of results to user
    queryString = null;
  }
  
  public static void main(final String... args) throws IOException {
    var filename = Files.readString(Paths.get("config.txt")).strip();
    new WebServer(PORT, filename);

  }
}
