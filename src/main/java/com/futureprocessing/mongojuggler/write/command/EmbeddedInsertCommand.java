package com.futureprocessing.mongojuggler.write.command;


import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.mongodb.BasicDBObject;

import java.util.function.Consumer;

import static com.futureprocessing.mongojuggler.commons.ProxyExtractor.extractInsertEmbeddedProxy;

public class EmbeddedInsertCommand extends AbstractInsertCommand {

    private final Class<?> embeddedType;

    public EmbeddedInsertCommand(String field, Class<?> embeddedType) {
        super(field);
        this.embeddedType = embeddedType;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        Object embedded = ProxyCreator.newInsertEmbeddedProxy(embeddedType, field, document);
        Consumer consumer = (Consumer) args[0];
        consumer.accept(embedded);
        extractInsertEmbeddedProxy(embedded).done();
    }

}
