package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.commons.Validator;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.read.command.*;

import java.lang.reflect.Method;
import java.util.Set;

import static com.futureprocessing.mongojuggler.commons.Validator.validateField;

public final class ReadMapper extends Mapper<ReadCommand> {

    public ReadMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected ReadCommand getCommand(Method method) {
        validateField(method);
        validateArguments(method);

        String field = getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            createMapping(method.getReturnType());
            return new EmbeddedReadCommand(field, method.getReturnType(), this);
        }

        if (isBooleanReturnType(method)) {
            return new BooleanReadCommand(field);
        }

        if (isSetReturnType(method)) {
            return new SetReadCommand(field);
        }

        if (field.equals("_id")) {
            return new IdReadCommand();
        }

        return new BasicReadCommand(field);
    }

    private void validateArguments(Method method) {
        if (method.getParameterCount() != 0) {
            throw new InvalidArgumentsException(method);
        }
    }

    private String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }

    private boolean isSetReturnType(Method method) {
        return Set.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isBooleanReturnType(Method method) {
        return method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class);
    }

}
