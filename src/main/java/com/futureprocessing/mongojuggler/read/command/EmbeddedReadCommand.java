package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class EmbeddedReadCommand implements ReadCommand {

    private final String field;
    private final Class clazz;

    public EmbeddedReadCommand(String field, Class clazz) {
        this.field = field;
        this.clazz = clazz;
    }

    @Override
    public Object read(BasicDBObject document) {
        return ProxyCreator.newReadProxy(clazz, (DBObject) document.get(field), unmodifiableSet(emptySet()));
    }
}
