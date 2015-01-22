package com.futureprocessing.mongojuggler.exception;

public class MissingPropertyException extends RuntimeException {
    public MissingPropertyException(String message) {
        super(message);
    }
}
