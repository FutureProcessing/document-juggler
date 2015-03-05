package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.read.ReaderMapper;
import com.futureprocessing.mongojuggler.read.ReadProxy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class EmbeddedReadCommand extends AbstractReadCommand {

    private final Class clazz;
    private final ReaderMapper mapper;

    public EmbeddedReadCommand(String field, Class clazz, ReaderMapper mapper) {
        super(field);
        this.clazz = clazz;
        this.mapper = mapper;
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        DBObject embedded = (DBObject) document.get(field);
        return embedded != null ? ReadProxy.create(clazz, mapper.get(clazz), embedded,
                                                   unmodifiableSet(emptySet())) : null;
    }
}
