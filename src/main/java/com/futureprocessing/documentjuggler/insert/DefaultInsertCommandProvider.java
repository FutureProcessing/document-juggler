package com.futureprocessing.documentjuggler.insert;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.BasicInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class DefaultInsertCommandProvider implements CommandProvider<InsertCommand> {
    @Override
    public InsertCommand getCommand(Method method, Mapper<InsertCommand> mapper) {
        return new BasicInsertCommand(getFieldName(method));
    }
}
