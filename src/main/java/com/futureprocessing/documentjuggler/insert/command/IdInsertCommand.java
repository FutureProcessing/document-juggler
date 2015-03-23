package com.futureprocessing.documentjuggler.insert.command;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

public class IdInsertCommand extends AbstractInsertCommand {
    public IdInsertCommand(String field) {
        super(field);
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        document.append(field, new ObjectId((String) args[0]));
    }
}
