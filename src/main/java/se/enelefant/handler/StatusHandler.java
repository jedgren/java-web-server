package se.enelefant.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import se.enelefant.helper.HandlerHelper;

import java.io.IOException;

public class StatusHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Server is up";
        HandlerHelper.writeResponse(response, httpExchange);
    }
}
