package com.futureprocessing.documentjuggler.query.command;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.mongodb.QueryBuilder;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class InQueryCommand implements QueryCommand {

    private final String field;

    public InQueryCommand(String field) {
        this.field = field;
    }

    public static void query(String field, QueryBuilder builder, Object value) {

    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        query(field, builder, args[0]);
    }


    public static class Provider implements CommandProvider<QueryCommand> {
        @Override
        public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
            return new InQueryCommand(getFieldName(method));
        }
    }
}
