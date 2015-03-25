package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.EmbeddedMapper;
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
        Class<?> returnType = method.getReturnType();

        EmbeddedMapper<ReadCommand> embeddedMapper = EmbeddedMapper.wrap(field, mapper);

        if (returnType.equals(List.class)) {
            Class embeddedType = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
            embeddedMapper.createMapping(embeddedType);
            return new EmbeddedListReadCommand(field, embeddedType, embeddedMapper);
        }

        if (returnType.equals(Set.class)) {
            Class embeddedType = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
            embeddedMapper.createMapping(embeddedType);
            return new EmbeddedSetReadCommand(field, embeddedType, embeddedMapper);
        }

        embeddedMapper.createMapping(returnType);
        return new EmbeddedReadCommand(field, method.getReturnType(), embeddedMapper);
    }
}
