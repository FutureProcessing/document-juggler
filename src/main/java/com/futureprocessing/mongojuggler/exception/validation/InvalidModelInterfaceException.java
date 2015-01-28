package com.futureprocessing.mongojuggler.exception.validation;


public abstract class InvalidModelInterfaceException extends RuntimeException {

    public InvalidModelInterfaceException(String message) {
        super(message);
    }
}
