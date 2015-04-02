package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.commons.AbstractMapper;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.futureprocessing.documentjuggler.update.command.ForbiddenUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateArraysOperatorsCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateOperatorsCommand;
import com.futureprocessing.documentjuggler.update.command.providers.DefaultUpdateCommandProvider;
import com.futureprocessing.documentjuggler.update.operators.Update;
import com.futureprocessing.documentjuggler.update.operators.UpdateArrays;

import java.lang.reflect.Method;
import java.util.Optional;

import static com.futureprocessing.documentjuggler.Context.UPDATE;
import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class UpdateMapper extends AbstractMapper<UpdateCommand> {

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
        return !hasCorrectReturnType(method) || field.equals("_id") || hasParameterOfType(method, Comparison.class);
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        return returnType.equals(clazz) || returnType.equals(Void.TYPE);
    }

    @Override
    protected Optional<UpdateCommand> getCommandBeforeAnnotationsReading(Method method) {
        Optional<UpdateCommand> command = getUpdateOperatorsCommand(method);
        return command;
    }

    private Optional<UpdateCommand> getUpdateOperatorsCommand(Method method) {
        if (method.getParameterCount() == 1) {
            Class<?> parameterType = method.getParameterTypes()[0];

            if (Update.class.isAssignableFrom(parameterType)) {
                return Optional.of(new UpdateOperatorsCommand(getFieldName(method)));
            }

            if (UpdateArrays.class.isAssignableFrom(parameterType)){
                return Optional.of(new UpdateArraysOperatorsCommand(getFieldName(method)));
            }

        }
        return Optional.empty();
    }
}
