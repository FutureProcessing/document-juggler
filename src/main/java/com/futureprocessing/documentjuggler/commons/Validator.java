package com.futureprocessing.documentjuggler.commons;


import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;

public final class Validator {

    public static void validateInterface(Class<?> model) {
        if (!model.isInterface()) {
            throw new ModelIsNotInterfaceException(model);
        }
    }
}
