package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.exception.validation.InvalidReturnValueException;
import com.futureprocessing.mongojuggler.read.command.BasicQueryCommand;
import com.futureprocessing.mongojuggler.read.command.IdQueryCommand;
import com.futureprocessing.mongojuggler.read.command.QueryCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.mongojuggler.commons.Validator.validateField;

public class QueryMapper extends Mapper<QueryCommand> {

    public QueryMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected QueryCommand getCommand(Method method) {
        validateField(method);
        validateReturnType(method);
        validateArguments(method);

        if (method.isAnnotationPresent(Id.class)) {
            return new IdQueryCommand();
        }

        String field = method.getAnnotation(DbField.class).value();
        return new BasicQueryCommand(field);

    }

    private void validateReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        if (!returnType.equals(clazz) && !returnType.equals(Void.TYPE)) {
            throw new InvalidReturnValueException(method);
        }
    }

    private void validateArguments(Method method) {
        if (method.getParameterCount() != 1) {
            throw new InvalidArgumentsException(method);
        }
    }
}
