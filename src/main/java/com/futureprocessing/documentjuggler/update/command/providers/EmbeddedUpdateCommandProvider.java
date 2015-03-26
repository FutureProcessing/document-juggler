package com.futureprocessing.documentjuggler.update.command.providers;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.EmbeddedUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static com.futureprocessing.documentjuggler.commons.ArgumentsReader.from;
import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class EmbeddedUpdateCommandProvider implements CommandProvider<UpdateCommand> {
    @Override
    public UpdateCommand getCommand(Method method, Mapper<UpdateCommand> mapper) {
        Class<?> type = from(method).getGenericType(0); // TODO Move to Embedded Update command
        mapper.createEmbeddedMapping(getFieldName(method), type);
        return new EmbeddedUpdateCommand(getFieldName(method), type, mapper);
    }

}
