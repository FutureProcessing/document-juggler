package com.futureprocessing.documentjuggler.query.command;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.QueryBuilder;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static com.futureprocessing.documentjuggler.commons.ArgumentsReader.from;
import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class EmbeddedQueryCommand implements QueryCommand {

    private final String field;
    private final QueryProcessor<?> processor;

    public EmbeddedQueryCommand(String field, QueryProcessor<?> processor) {
        this.field = field;
        this.processor = processor;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).is(processor.process((Consumer) args[0]));
    }

    public static class Provider implements CommandProvider<QueryCommand> {

        @SuppressWarnings("unchecked")
        @Override
        public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
            Class<?> type = from(method).getGenericType(0);
            mapper.createEmbeddedMapping(getFieldName(method), type);
            QueryProcessor<?> processor = new QueryProcessor(type, mapper);
            return new EmbeddedQueryCommand(getFieldName(method), processor);
        }

    }
}
