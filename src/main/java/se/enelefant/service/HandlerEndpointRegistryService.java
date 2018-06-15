package se.enelefant.service;


import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import se.enelefant.core.annotation.HandlerEndpoint;
import se.enelefant.core.annotation.ResponseStatus;
import se.enelefant.core.enums.HttpStatus;
import se.enelefant.core.enums.RequestMethod;
import se.enelefant.core.handler.WebServerHandler;
import se.enelefant.helper.LoggerHelper;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class HandlerEndpointRegistryService {

    private static final Logger logger = LoggerHelper.getMainLogger();
    private static final String PKG_SEPARATOR = ".";
    private static final String CLASS_FILE_SUFFIX = ".class";
    private static final String DIR_SEPARATOR = "/";
    private static final String BAD_PACKAGE_ERROR = "";

    public void registerEndpoints(HttpServer server) throws Exception {
        logger.info("Registering endpoints...");
        Map<EndpointMetaData, Class<HttpHandler>> endpoints = findEndpoints(new HashMap<>(), "se.enelefant.handler");

        List<Filter> handlerFilters = findHandlerFilters(new ArrayList<>(), "se.enelefant.handler.filter");

        for (EndpointMetaData endpointMetaData : endpoints.keySet()) {
            Class<HttpHandler> httpHandlerClass = endpoints.get(endpointMetaData);
            HttpContext context = server.createContext(endpointMetaData.endpoint, httpHandlerClass.newInstance());
            context.getAttributes().put(WebServerHandler.ACCEPTED_METHOD_KEY, endpointMetaData.requestMethod);
            context.getAttributes().put(WebServerHandler.RESPONSE_STATUS_KEY, endpointMetaData.responseStatus);
            for (Filter filter : handlerFilters) {
                context.getFilters().add(filter);
            }
            logger.info("Registering " + httpHandlerClass.getSimpleName() + ", endpoint = '" + endpointMetaData.endpoint + "', method = '" + endpointMetaData.requestMethod.getStringMethod() + "'");
        }
    }

    private List<Filter> findHandlerFilters(List<Class<Filter>> filterClasses, String scannedPackage) throws IllegalAccessException, InstantiationException {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = ClassLoader.getSystemClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        for (File file : scannedDir.listFiles()) {
            findFilters(filterClasses, file, scannedPackage);
        }
        List<Filter> filters = new ArrayList<>();
        for (Class<Filter> filterClass : filterClasses) {
            Filter filter = filterClass.newInstance();
            logger.info("Registering filter " + filterClass.getSimpleName());
            filters.add(filter);
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
                if (Filter.class.isAssignableFrom(potentialClass)) {
                    filters.add((Class<Filter>) potentialClass);
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
    }

    private Map<EndpointMetaData, Class<HttpHandler>> findEndpoints(Map<EndpointMetaData, Class<HttpHandler>> endpoints, String scannedPackage) {
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

    private void findEndpoints(Map<EndpointMetaData, Class<HttpHandler>> endpoints, File file, String scannedPackage) {
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
                        ResponseStatus responseStatusAnnotation = method.getAnnotation(ResponseStatus.class);
                        if (annotation != null) {
                            EndpointMetaData metaData = new EndpointMetaData(annotation.value(), annotation.method());
                            if (responseStatusAnnotation != null) {
                                metaData.responseStatus = responseStatusAnnotation.value();
                            } else {
                                metaData.responseStatus = HttpStatus.OK;
                            }
                            endpoints.put(metaData, (Class<HttpHandler>) potentialClass);
                        }
                    }
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
    }

    private class EndpointMetaData {
        String endpoint;
        RequestMethod requestMethod;
        HttpStatus responseStatus;

        public EndpointMetaData(String endpoint, RequestMethod requestMethod) {
            this.endpoint = endpoint;
            this.requestMethod = requestMethod;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EndpointMetaData that = (EndpointMetaData) o;
            return Objects.equals(endpoint, that.endpoint) &&
                    requestMethod == that.requestMethod;
        }

        @Override
        public int hashCode() {
            return Objects.hash(endpoint, requestMethod);
        }
    }
}
