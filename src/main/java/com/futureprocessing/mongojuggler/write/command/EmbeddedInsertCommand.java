package com.futureprocessing.mongojuggler.write.command;


import com.futureprocessing.mongojuggler.write.InsertMapper;
import com.futureprocessing.mongojuggler.write.InsertProxy;
import com.mongodb.BasicDBObject;

import java.util.function.Consumer;

public class EmbeddedInsertCommand extends AbstractInsertCommand {

    private final Class<?> embeddedType;
    private final InsertMapper mapper;

    public EmbeddedInsertCommand(String field, Class<?> embeddedType, InsertMapper mapper) {
        super(field);
        this.embeddedType = embeddedType;
        this.mapper = mapper;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        Object embedded = InsertProxy.create(embeddedType, mapper.get(embeddedType));
        Consumer consumer = (Consumer) args[0];
        consumer.accept(embedded);

        document.put(field, InsertProxy.extract(embedded).getDocument());
    }

}
