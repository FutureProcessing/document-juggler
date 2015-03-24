package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.annotation.*;
import com.futureprocessing.documentjuggler.annotation.internal.UpdateContext;
import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.*;
import com.futureprocessing.documentjuggler.update.command.providers.DefaultUpdateCommandProvider;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.UPDATE;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public class UpdateMapper extends Mapper<UpdateCommand> {

    public static <MODEL> UpdateMapper map(Class<MODEL> modelClass) {
        UpdateMapper mapper = new UpdateMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }

    private UpdateMapper(Class clazz) {
        super(clazz, new DefaultUpdateCommandProvider());
    }

    @Override
    protected UpdateCommand getCommand(Method method) {
        AnnotationReader annotationReader = from(method);
        String field = FieldNameExtractor.getFieldName(method);

        if (isForbidden(method, UPDATE) || !hasCorrectReturnType(method) || field.equals("_id")) {
            return new ForbiddenUpdateCommand(method);
        }

        UpdateContext context = from(method).read(UpdateContext.class);
        if (context != null) {
            Class<? extends CommandProvider<UpdateCommand>> commandClass = context.commandProvider();

            try {
                CommandProvider<UpdateCommand> commandProvider = commandClass.newInstance();
                return commandProvider.getCommand(method, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return getDefaultCommand(method);
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        return returnType.equals(clazz) || returnType.equals(Void.TYPE);
    }
}
