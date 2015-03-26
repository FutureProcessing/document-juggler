package com.futureprocessing.documentjuggler.query.command;


import com.futureprocessing.documentjuggler.commons.ArgumentsReader;
import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.QueryBuilder;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;

import static com.futureprocessing.documentjuggler.commons.ArgumentsReader.from;
import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class EmbeddedQueryCommand implements QueryCommand {

    private final Method method;
    private final String field;
    private final Mapper<QueryCommand> mapper;
    private QueryProcessor processor;
    private final Class type;

    public EmbeddedQueryCommand(Method method, String field, Mapper<QueryCommand> mapper, Class type) {
        this.method = method;
        this.field = field;
        this.mapper = mapper;

        this.type = type;

    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        processor = new QueryProcessor(type, mapper);
        builder.and(field).is(processor.process((Consumer) args[0]));
    }

    public static class Provider implements CommandProvider<QueryCommand> {
        @Override
        public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
            Class<?> type = from(method).getGenericType(0);
            mapper.createEmbeddedMapping(getFieldName(method), type);
            return new EmbeddedQueryCommand(method, getFieldName(method), mapper, type);
        }

    }
}
