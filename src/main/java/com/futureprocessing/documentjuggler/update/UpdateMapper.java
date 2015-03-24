package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.ForbiddenChecker;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.*;
import com.futureprocessing.documentjuggler.update.command.providers.DefaultUpdateCommandProvider;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.UPDATE;

public class UpdateMapper extends Mapper<UpdateCommand> {

    public static <MODEL> UpdateMapper map(Class<MODEL> modelClass) {
        UpdateMapper mapper = new UpdateMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }

    private UpdateMapper(Class clazz) {
        super(UPDATE, new DefaultUpdateCommandProvider());
    }

    @Override
    protected UpdateCommand getForbidden(Method method) {

        String field = FieldNameExtractor.getFieldName(method);

        if (ForbiddenChecker.isForbidden(method, UPDATE) || !hasCorrectReturnType(method) || field.equals("_id")) {
            return new ForbiddenUpdateCommand(method);
        }

        return null;
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        return returnType.equals(clazz) || returnType.equals(Void.TYPE);
    }
}
