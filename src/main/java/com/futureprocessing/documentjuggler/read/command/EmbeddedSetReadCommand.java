package com.futureprocessing.documentjuggler.read.command;


import com.futureprocessing.documentjuggler.read.ReadMapper;
import com.futureprocessing.documentjuggler.read.ReadProxy;
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
                .map(el -> ReadProxy.create(clazz, mapper.get(), (DBObject) el, unmodifiableSet(emptySet())))
                .collect(toSet()));

    }
}
