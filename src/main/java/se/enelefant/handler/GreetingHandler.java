package se.enelefant.handler;

import com.sun.net.httpserver.HttpExchange;
import se.enelefant.core.annotation.HandlerEndpoint;
import se.enelefant.core.enums.RequestMethod;
import se.enelefant.core.handler.WebServerHandler;

import java.io.IOException;

public class GreetingHandler extends WebServerHandler {

    @Override
    @HandlerEndpoint(value = "/", method = RequestMethod.GET)
    public void handleRequest(HttpExchange httpExchange) throws IOException {
        String response = "Greetings!";
        writeResponse(response, httpExchange);
    }
}
