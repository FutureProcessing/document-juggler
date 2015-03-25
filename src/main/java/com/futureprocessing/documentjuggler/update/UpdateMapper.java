package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.ForbiddenUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;
import com.futureprocessing.documentjuggler.update.command.providers.DefaultUpdateCommandProvider;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.UPDATE;

public class UpdateMapper extends Mapper<UpdateCommand> {

    public static <MODEL> UpdateMapper map(Class<MODEL> modelClass) {
        UpdateMapper mapper = new UpdateMapper();
        mapper.createMapping(modelClass);
        return mapper;
    }

    private UpdateMapper() {
        super(UPDATE, new DefaultUpdateCommandProvider(), new ForbiddenUpdateCommand.Provider());
    }

    @Override
    protected boolean isForbidden(Method method) {
        String field = FieldNameExtractor.getFieldName(method);
        return !hasCorrectReturnType(method) || field.equals("_id");
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        return returnType.equals(clazz) || returnType.equals(Void.TYPE);
    }
}
