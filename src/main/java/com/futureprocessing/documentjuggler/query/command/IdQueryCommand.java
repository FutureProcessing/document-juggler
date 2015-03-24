package com.futureprocessing.documentjuggler.query.command;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.mongodb.QueryBuilder;
import org.bson.types.ObjectId;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class IdQueryCommand implements QueryCommand {

    private final String field;

    public IdQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).is(new ObjectId((String) args[0]));
    }

    public static class Provider implements CommandProvider<QueryCommand>{
        @Override
        public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
            return new IdQueryCommand(getFieldName(method));
        }
    }
}
