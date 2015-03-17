package com.futureprocessing.documentjuggler.query.command;


import com.mongodb.QueryBuilder;
import org.bson.types.ObjectId;

public class IdQueryCommand implements QueryCommand {

    private final String field;

    public IdQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).is(new ObjectId((String) args[0]));
    }
}
