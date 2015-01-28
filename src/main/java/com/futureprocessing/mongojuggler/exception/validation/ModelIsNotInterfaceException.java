package com.futureprocessing.mongojuggler.exception.validation;


import static java.lang.String.format;

public class ModelIsNotInterfaceException extends InvalidModelInterfaceException{

    private final Class<?> clazz;

    public ModelIsNotInterfaceException(Class<?> clazz) {
        super(format("Class %s is not interface", clazz.getName()));
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

}
