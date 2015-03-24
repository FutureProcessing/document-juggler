package com.futureprocessing.documentjuggler.query.command.providers;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.BasicQueryCommand;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class DefaultQueryCommandProvider implements CommandProvider<QueryCommand> {
    @Override
    public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
        return new BasicQueryCommand(getFieldName(method));
    }
}
