package com.futureprocessing.documentjuggler.insert.command;


import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.InsertMapper;
import com.futureprocessing.documentjuggler.insert.InsertProxy;
import com.mongodb.BasicDBObject;

import java.util.function.Consumer;

public class EmbeddedInsertCommand extends AbstractInsertCommand {

    private final Class<?> embeddedType;
    private final Mapper<InsertCommand> mapper;

    public EmbeddedInsertCommand(String field, Class<?> embeddedType, Mapper<InsertCommand> mapper) {
        super(field);
        this.embeddedType = embeddedType;
        this.mapper = mapper;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        Object embedded = InsertProxy.create(embeddedType, mapper.get());
        Consumer consumer = (Consumer) args[0];
        consumer.accept(embedded);

        document.put(field, InsertProxy.extract(embedded).getDocument());
    }

}
