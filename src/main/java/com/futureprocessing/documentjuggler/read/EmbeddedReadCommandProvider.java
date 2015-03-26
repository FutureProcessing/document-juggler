package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.EmbeddedListReadCommand;
import com.futureprocessing.documentjuggler.read.command.EmbeddedReadCommand;
import com.futureprocessing.documentjuggler.read.command.EmbeddedSetReadCommand;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

public class EmbeddedReadCommandProvider implements CommandProvider<ReadCommand> {
    @Override
    public ReadCommand getCommand(Method method, Mapper<ReadCommand> mapper) {

        String field = FieldNameExtractor.getFieldName(method);
        Class<?> embeddedType = method.getReturnType();

        if (embeddedType.equals(List.class)) {
            embeddedType = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
            mapper.createEmbeddedMapping(field, embeddedType);
            return new EmbeddedListReadCommand(field, embeddedType, mapper);
        }

        if (embeddedType.equals(Set.class)) {
            embeddedType = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
            mapper.createEmbeddedMapping(field, embeddedType);
            return new EmbeddedSetReadCommand(field, embeddedType, mapper);
        }

        mapper.createEmbeddedMapping(field, embeddedType);
        return new EmbeddedReadCommand(field, method.getReturnType(), mapper);
    }
}
