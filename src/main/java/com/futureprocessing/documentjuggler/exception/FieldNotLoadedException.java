package com.futureprocessing.documentjuggler.exception;

import static java.lang.String.format;

public class FieldNotLoadedException extends RuntimeException {

    public FieldNotLoadedException(String field) {
        super(format("Field [%s] not loaded", field));
    }
}
