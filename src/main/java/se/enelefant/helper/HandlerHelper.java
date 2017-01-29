package se.enelefant.helper;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HandlerHelper {

    public static void writeResponse(String response, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
