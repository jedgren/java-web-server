package se.enelefant.handler.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import se.enelefant.helper.LoggerHelper;

import java.io.IOException;
import java.util.logging.Logger;

public class AccessLoggerFilter extends Filter {

    private static final Logger logger = LoggerHelper.getMainLogger();

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        logger.fine(String.format("[%s] access '%s'", httpExchange.getRemoteAddress().getAddress().getHostAddress(), httpExchange.getRequestURI().getPath()));
        chain.doFilter(httpExchange);
    }

    @Override
    public String description() {
        return "Access logger on level FINEST filter for all requests";
    }
}
