package com.futureprocessing.documentjuggler.insert;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.EmbeddedInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.EmbeddedVarArgInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class EmbeddedInsertCommandProvider implements CommandProvider<InsertCommand> {
    @Override
    public InsertCommand getCommand(Method method, Mapper<InsertCommand> mapper) {
        String field = FieldNameExtractor.getFieldName(method);

        if (method.isVarArgs()) {
            Class<?> type = getEmbeddedListDocumentType(method);
            mapper.createMapping(type);
            return new EmbeddedVarArgInsertCommand(field, type, mapper);
        }

        Class<?> type = getEmbeddedDocumentType(method);
        mapper.createMapping(type);
        return new EmbeddedInsertCommand(field, type, mapper);
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
