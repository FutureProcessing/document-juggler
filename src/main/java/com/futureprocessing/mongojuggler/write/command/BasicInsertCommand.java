package com.futureprocessing.mongojuggler.write.command;


import com.mongodb.BasicDBObject;

public class BasicInsertCommand extends AbstractInsertCommand {

    public BasicInsertCommand(String field) {
        super(field);
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        document.append(field, args[0]);
    }
}
