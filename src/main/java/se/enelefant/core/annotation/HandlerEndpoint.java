package se.enelefant.core.annotation;

import se.enelefant.core.enums.RequestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerEndpoint {

    String value();

    RequestMethod method() default RequestMethod.ANY;
}
