package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.annotation.AnnotationReader;
import com.futureprocessing.documentjuggler.annotation.internal.ReadContext;
import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.ForbiddenReadCommand;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.READ;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public final class ReadMapper extends Mapper<ReadCommand> {

    public static <MODEL> ReadMapper map(Class<MODEL> modelClass) {
        ReadMapper mapper = new ReadMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }

    private ReadMapper(Class clazz) {
        super(clazz, new DefaultReadCommandProvider());
    }

    @Override
    protected ReadCommand getCommand(Method method) {
        if (isForbidden(method, READ) || !hasCorrectParameters(method)) {
            return new ForbiddenReadCommand(method);
        }

        ReadContext readContext = from(method).read(ReadContext.class);
        if (readContext != null) {
            Class<? extends CommandProvider<ReadCommand>> commandClass = readContext.commandProvider();

            try {
                CommandProvider<ReadCommand> commandProvider = commandClass.newInstance();
                return commandProvider.getCommand(method, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return getDefaultCommand(method);

    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 0;
    }
}
