package com.futureprocessing.documentjuggler.query.command;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.mongodb.QueryBuilder;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class GreaterThanEqualQueryCommand implements QueryCommand {

    private final String field;

    public GreaterThanEqualQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).greaterThanEquals(args[0]);
    }

    public static class Provider implements CommandProvider<QueryCommand> {
        @Override
        public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
            return new GreaterThanEqualQueryCommand(getFieldName(method));
        }
    }
}
