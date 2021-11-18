package searchengine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpExchange;

/**
 * A class that contains one method for sending a response to the webserver. 
 * 
 * @author DIHJ, SULE, LINJ and SIKO
 * @version 1.0
 * @since 13/12 - 2020 
 * 
 * */

public class Response {
  static final Charset CHARSET = StandardCharsets.UTF_8;

  /** Send info to server. Could benefit from new name
   * @param io 
   * @param code 
   * @param mime
   * @param response
   */
  void respond(HttpExchange io, int code, String mime, byte[] response) {
    try {
      io.getResponseHeaders().set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
      io.sendResponseHeaders(200, response.length);
      io.getResponseBody().write(response);
    } catch (Exception e) {
    } finally {
      io.close();
    }
  }

}
