package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdateMapper;
import com.futureprocessing.documentjuggler.update.UpdateProxy;

import java.util.function.Consumer;

public class EmbeddedUpdateCommand extends AbstractUpdateCommand {

    private final Class clazz;
    private final UpdateMapper mapper;

    public EmbeddedUpdateCommand(String field, Class clazz, UpdateMapper mapper) {
        super(field);
        this.clazz = clazz;
        this.mapper = mapper;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object embeddedProxy = UpdateProxy.create(clazz, mapper.get(), updateBuilder.embedded(field));
        Consumer embeddedDocumentModifier = (Consumer) args[0];
        embeddedDocumentModifier.accept(embeddedProxy);
    }
}
