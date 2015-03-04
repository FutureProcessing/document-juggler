package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.futureprocessing.mongojuggler.read.ReadProxy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

public class EmbeddedSetReadCommand extends AbstractReadCommand {

    private final Class clazz;
    private final ReadMapper mapper;

    public EmbeddedSetReadCommand(String field, Class clazz, ReadMapper mapper) {
        super(field);
        this.clazz = clazz;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object readValue(BasicDBObject document) {
        List list = (List) document.get(field);

        return list == null ? null : unmodifiableSet((Set<?>) list.stream()
                .map(el -> ReadProxy.create(clazz, mapper.get(clazz), (DBObject) el, unmodifiableSet(emptySet())))
                .collect(toSet()));

    }
}
