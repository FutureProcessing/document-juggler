package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.BasicReadCommand;
import com.futureprocessing.documentjuggler.read.command.BooleanReadCommand;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;
import com.futureprocessing.documentjuggler.read.command.SetReadCommand;

import java.lang.reflect.Method;
import java.util.Set;

public class DefaultReadCommandProvider implements CommandProvider<ReadCommand> {

    @Override
    public ReadCommand getCommand(Method method, Mapper<ReadCommand> mapper) {

        String field = FieldNameExtractor.getFieldName(method);

        if (isBooleanReturnType(method)) {
            return new BooleanReadCommand(field);
        }

        if (isSetReturnType(method)) {
            return new SetReadCommand(field);
        }

        return new BasicReadCommand(field);
    }

    private boolean isSetReturnType(Method method) {
        return Set.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isBooleanReturnType(Method method) {
        return method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class);
    }

}
