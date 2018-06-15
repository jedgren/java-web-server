package se.enelefant.core.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import se.enelefant.core.enums.HttpStatus;
import se.enelefant.core.enums.RequestMethod;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public abstract class WebServerHandler implements HttpHandler {

    public static final String ACCEPTED_METHOD_KEY = "accept-method";
    public static final String RESPONSE_STATUS_KEY = "response-status";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (isCorrectRequestMethod(httpExchange)) {
            handleRequest(httpExchange);
        } else {
            setHttpStatus(httpExchange, HttpStatus.METHOD_NOT_ALLOWED);
            writeResponse("", httpExchange);
        }
    }

    protected abstract void handleRequest(HttpExchange httpExchange) throws IOException;

    protected void writeResponse(String response, HttpExchange httpExchange) throws IOException {

        HttpStatus httpStatus = getHttpStatus(httpExchange);

        httpExchange.sendResponseHeaders(httpStatus.getStatusCode(), response.length());

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        } catch (IOException e) {
            throw e;
        }
    }

    private boolean isCorrectRequestMethod(HttpExchange httpExchange) {
        return httpExchange.getRequestMethod().equalsIgnoreCase(getRequestMethod(httpExchange).getStringMethod());
    }

    private RequestMethod getRequestMethod(HttpExchange httpExchange) {
        return getHttpAttribute(httpExchange, ACCEPTED_METHOD_KEY, RequestMethod.class);
    }

    private HttpStatus getHttpStatus(HttpExchange httpExchange) {
        return getHttpAttribute(httpExchange, RESPONSE_STATUS_KEY, HttpStatus.class);
    }

    private void setHttpStatus(HttpExchange httpExchange, HttpStatus httpStatus) {
        httpExchange.setAttribute(RESPONSE_STATUS_KEY, httpStatus);
    }

    private <T> T getHttpAttribute(HttpExchange httpExchange, String key, Class<T> castClass) {
        return (T) httpExchange.getAttribute(key);
    }

}
