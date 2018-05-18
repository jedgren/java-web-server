package se.enelefant.helper;

public class PropertyHelper {

    public static int getPort() {
        String port = System.getProperty("se.enelefant.web-server.port");
        if (port != null && port.matches("%d")) {
            return Integer.getInteger(port);
        }
        return 8000;
    }

}
