package com.futureprocessing.documentjuggler.insert;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.EmbeddedInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.EmbeddedVarArgInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.ArgumentsReader.from;

public class EmbeddedInsertCommandProvider implements CommandProvider<InsertCommand> {
    @Override
    public InsertCommand getCommand(Method method, Mapper<InsertCommand> mapper) {
        String field = FieldNameExtractor.getFieldName(method);
        Class<?> type = from(method).getGenericType(0);

        if (method.isVarArgs()) {
            mapper.createEmbeddedMapping(field, type);
            return new EmbeddedVarArgInsertCommand(field, type, mapper);
        }

        mapper.createEmbeddedMapping(field, type);
        return new EmbeddedInsertCommand(field, type, mapper);
    }


}
