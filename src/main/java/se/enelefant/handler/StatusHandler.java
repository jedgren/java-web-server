package se.enelefant.handler;

import com.sun.net.httpserver.HttpExchange;
import se.enelefant.core.annotation.HandlerEndpoint;
import se.enelefant.core.annotation.ResponseStatus;
import se.enelefant.core.enums.HttpStatus;
import se.enelefant.core.enums.RequestMethod;
import se.enelefant.core.handler.WebServerHandler;

import java.io.IOException;

public class StatusHandler extends WebServerHandler {

    @Override
    @HandlerEndpoint(value = "/status", method = RequestMethod.GET)
    public void handleRequest(HttpExchange httpExchange) throws IOException {
        String response = "Server is up";
        writeResponse(response, httpExchange);
    }
}
