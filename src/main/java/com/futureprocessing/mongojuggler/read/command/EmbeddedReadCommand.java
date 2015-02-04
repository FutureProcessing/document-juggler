package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class EmbeddedReadCommand extends AbstractReadCommand {

    private final Class clazz;

    public EmbeddedReadCommand(String field, Class clazz) {
        super(field);
        this.clazz = clazz;
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        DBObject embedded = (DBObject) document.get(field);
        return embedded != null ? ProxyCreator.newReadProxy(clazz, embedded, unmodifiableSet(emptySet())) : null;
    }
}
