package se.enelefant.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class StatusHandler extends WebServerHandler {

    @Override
    @HandlerEndpoint("/status")
    public void handleRequest(HttpExchange httpExchange) throws IOException {
        String response = "Server is up";
        writeResponse(response, httpExchange);
    }
}
