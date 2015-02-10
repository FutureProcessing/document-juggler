package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class EmbeddedReadCommand extends AbstractReadCommand {

    private final Class clazz;
    private final ReadMapper mapper;

    public EmbeddedReadCommand(String field, Class clazz, ReadMapper mapper) {
        super(field);
        this.clazz = clazz;
        this.mapper = mapper;
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        DBObject embedded = (DBObject) document.get(field);
        return embedded != null ? ProxyCreator.newReadProxy(clazz, mapper, embedded, unmodifiableSet(emptySet())) : null;
    }
}
