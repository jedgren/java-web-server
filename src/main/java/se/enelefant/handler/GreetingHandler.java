package se.enelefant.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class GreetingHandler extends WebServerHandler {

    @Override
    @HandlerEndpoint("/")
    public void handleRequest(HttpExchange httpExchange) throws IOException {
        String response = "Greetings!";
        writeResponse(response, httpExchange);
    }
}
