package se.enelefant.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import se.enelefant.helper.HandlerHelper;

import java.io.IOException;

public class GreetingHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Greetings!";
        HandlerHelper.writeResponse(response, httpExchange);
    }
}
