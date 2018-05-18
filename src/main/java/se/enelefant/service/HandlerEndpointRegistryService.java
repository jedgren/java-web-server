package se.enelefant.service;


import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import se.enelefant.handler.HandlerEndpoint;
import se.enelefant.handler.filter.HandlerFilter;
import se.enelefant.helper.LoggerHelper;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HandlerEndpointRegistryService {

    private static final Logger logger = LoggerHelper.getMainLogger();
    private static final String PKG_SEPARATOR = ".";
    private static final String CLASS_FILE_SUFFIX = ".class";
    private static final String DIR_SEPARATOR = "/";
    private static final String BAD_PACKAGE_ERROR = "";

    public void registerEndpoints(HttpServer server) throws Exception {
        logger.info("Registering endpoints...");
        Map<String, Class<HttpHandler>> endpoints = findEndpoints(new HashMap<>(), "se.enelefant.handler");
        for (String path : endpoints.keySet()) {
            Class<HttpHandler> httpHandlerClass = endpoints.get(path);
            logger.info("Registering " + httpHandlerClass.getSimpleName() + " at endpoint '" + path + "'");
        }
    }

    private List<Class<Filter>> getHandlerFilters(List<Class<Filter>> filters, String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = ClassLoader.getSystemClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        for (File file : scannedDir.listFiles()) {
            findFilters(filters, file, scannedPackage);
        }
        return filters;
    }

    private void findFilters(List<Class<Filter>> filters, File file, String scannedPackage) {
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                findFilters(filters, child, resource);
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                Class<?> potentialClass = Class.forName(className);
                if (HttpHandler.class.isAssignableFrom(potentialClass)) {
                    for (Method method : potentialClass.getMethods()) {
                        HandlerFilter annotation = method.getAnnotation(HandlerFilter.class);
                        if (annotation != null) {
                            filters.add((Class<Filter>) potentialClass);
                        }
                    }
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
    }

    private Map<String, Class<HttpHandler>> findEndpoints(Map<String, Class<HttpHandler>> endpoints, String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = ClassLoader.getSystemClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        for (File file : scannedDir.listFiles()) {
            findEndpoints(endpoints, file, scannedPackage);
        }
        return endpoints;
    }

    private void findEndpoints(Map<String, Class<HttpHandler>> endpoints, File file, String scannedPackage) {
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                findEndpoints(endpoints, child, resource);
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                Class<?> potentialClass = Class.forName(className);
                if (HttpHandler.class.isAssignableFrom(potentialClass)) {
                    for (Method method : potentialClass.getMethods()) {
                        HandlerEndpoint annotation = method.getAnnotation(HandlerEndpoint.class);
                        if (annotation != null) {
                            endpoints.put(annotation.value(), (Class<HttpHandler>) potentialClass);
                        }
                    }
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
    }

}
