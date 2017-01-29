package se.enelefant;

import com.sun.net.httpserver.HttpServer;
import se.enelefant.handler.GreetingHandler;
import se.enelefant.handler.StatusHandler;

import java.net.InetSocketAddress;

public class JavaWebServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new GreetingHandler());
        server.createContext("/status", new StatusHandler());
        server.setExecutor(null);
        server.start();
    }

}
