package com.futureprocessing.mongojuggler.write.command;


import com.mongodb.BasicDBObject;

import java.util.function.Consumer;

import static com.futureprocessing.mongojuggler.commons.ProxyCreator.newInsertProxy;
import static com.futureprocessing.mongojuggler.commons.ProxyExtractor.extractInsertProxy;

public class EmbeddedInsertCommand extends AbstractInsertCommand {

    private final Class<?> embeddedType;

    public EmbeddedInsertCommand(String field, Class<?> embeddedType) {
        super(field);
        this.embeddedType = embeddedType;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        Object embedded = newInsertProxy(embeddedType);
        Consumer consumer = (Consumer) args[0];
        consumer.accept(embedded);

        document.put(field, extractInsertProxy(embedded).getDocument());
    }

}
