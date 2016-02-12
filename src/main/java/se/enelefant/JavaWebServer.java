package se.enelefant;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class JavaWebServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.setExecutor(null);
        server.start();
    }

}
