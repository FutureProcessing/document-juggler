package com.futureprocessing.documentjuggler.update.command.providers;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.EmbeddedUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class EmbeddedUpdateCommandProvider implements CommandProvider<UpdateCommand> {
    @Override
    public UpdateCommand getCommand(Method method, Mapper<UpdateCommand> mapper) {
        Class<?> type = method.isVarArgs() ? getEmbeddedListDocumentType(method) : getEmbeddedDocumentType(method);
        mapper.createMapping(type);
        return new EmbeddedUpdateCommand(getFieldName(method), type, mapper);
    }

    private Class<?> getEmbeddedDocumentType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private Class<?> getEmbeddedListDocumentType(Method method) {
        GenericArrayType type = (GenericArrayType) method.getGenericParameterTypes()[0];
        return (Class<?>) ((ParameterizedType) type.getGenericComponentType()).getActualTypeArguments()[0];
    }
}
