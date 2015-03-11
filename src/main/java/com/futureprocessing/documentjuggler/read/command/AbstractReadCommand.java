package com.futureprocessing.documentjuggler.read.command;


import com.futureprocessing.documentjuggler.exception.FieldNotLoadedException;
import com.mongodb.BasicDBObject;

import java.util.Set;

public abstract class AbstractReadCommand implements ReadCommand {

    protected final String field;

    protected AbstractReadCommand(String field) {
        this.field = field;
    }

    @Override
    public final Object read(BasicDBObject document, Set<String> queriedFields) {
        if (queriedFields.isEmpty() || queriedFields.contains(field)) {
            return readValue(document);
        }

        throw new FieldNotLoadedException(field);
    }

    protected abstract Object readValue(BasicDBObject document);

}
