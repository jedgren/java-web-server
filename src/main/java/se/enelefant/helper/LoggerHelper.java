package se.enelefant.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerHelper {

    private static Logger initializedLogger = null;

    public static Logger getMainLogger() {
        if (initializedLogger == null) {
            Logger logger = Logger.getLogger("JavaWebServer");
            logger.finest("initializing - trying to load configuration file ...");

            try {
                InputStream configFile = LoggerHelper.class.getResourceAsStream("/logger.properties");
                LogManager.getLogManager().readConfiguration(configFile);

            } catch (IOException ex)
            {
                System.out.println("WARNING: Could not open configuration file");
                System.out.println("WARNING: Logging not configured (console output only)");
            }

            initializedLogger = logger;
        }

        return initializedLogger;
    }

}
