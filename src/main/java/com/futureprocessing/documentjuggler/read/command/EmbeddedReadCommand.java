package com.futureprocessing.documentjuggler.read.command;


import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.ReadProxy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class EmbeddedReadCommand extends AbstractReadCommand {

    private final Class clazz;
    private final Mapper<ReadCommand> mapper;

    public EmbeddedReadCommand(String field, Class clazz, Mapper<ReadCommand> mapper) {
        super(field);
        this.clazz = clazz;
        this.mapper = mapper;
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        DBObject embedded = (DBObject) document.get(field);
        return embedded != null ? ReadProxy.create(clazz, mapper, embedded, unmodifiableSet(emptySet())) : null;
    }
}
