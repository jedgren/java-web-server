package se.enelefant.core.annotation;

import se.enelefant.core.enums.HttpStatus;

public @interface ResponseStatus {

    HttpStatus value() default HttpStatus.OK;

}
