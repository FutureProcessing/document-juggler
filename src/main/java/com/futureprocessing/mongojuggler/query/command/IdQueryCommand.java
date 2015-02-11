package com.futureprocessing.mongojuggler.query.command;


import com.mongodb.QueryBuilder;
import org.bson.types.ObjectId;

public class IdQueryCommand implements QueryCommand {

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and("_id").is(new ObjectId((String) args[0]));
    }
}
