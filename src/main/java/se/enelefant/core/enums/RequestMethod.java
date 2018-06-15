package se.enelefant.core.enums;

public enum RequestMethod {

    ANY,
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE,
    PATCH;

    public String getStringMethod() {
        return this.name().toLowerCase();
    }
}
