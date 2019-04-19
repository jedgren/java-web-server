package se.enelefant.helper;

public class PropertyHelper {

    public static int getPort() {
        String port = System.getProperty("se.enelefant.web-server.port");
        if (port != null && port.matches("%d")) {
            return Integer.getInteger(port);
        }
        return 8000;
    }

    public static String getHandlerPackage() {
        return System.getProperty("se.enelefant.web-server.handlerPackage", "se.enelefant.handler");
    }

    public static String getFilterPackage() {
        return System.getProperty("se.enelefant.web-server.filterPackage", "se.enelefant.handler.filter");
    }

}
