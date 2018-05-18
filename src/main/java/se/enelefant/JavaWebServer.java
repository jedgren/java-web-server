package se.enelefant;

import com.sun.net.httpserver.HttpServer;
import se.enelefant.helper.LoggerHelper;
import se.enelefant.helper.PropertyHelper;
import se.enelefant.service.HandlerEndpointRegistryService;

import java.net.InetSocketAddress;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class JavaWebServer {

    private static final Logger logger = LoggerHelper.getMainLogger();

    public static void main(String[] args) throws Exception {
        long t0 = System.currentTimeMillis();

        logger.setResourceBundle(ResourceBundle.getBundle("logger"));

        HttpServer server = HttpServer.create(new InetSocketAddress(PropertyHelper.getPort()), 0);

        HandlerEndpointRegistryService registryService = new HandlerEndpointRegistryService();
        registryService.registerEndpoints(server);

        server.setExecutor(null);
        server.start();
        logger.info("Server started in " + (System.currentTimeMillis() - t0) + "ms");
    }

}
