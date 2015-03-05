package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.update.UpdateBuilder;
import com.futureprocessing.mongojuggler.update.UpdaterMapper;
import com.futureprocessing.mongojuggler.update.UpdateProxy;

import java.util.function.Consumer;

public class EmbeddedUpdateCommand extends AbstractUpdateCommand {

    private final Class clazz;
    private final UpdaterMapper mapper;

    public EmbeddedUpdateCommand(String field, Class clazz, UpdaterMapper mapper) {
        super(field);
        this.clazz = clazz;
        this.mapper = mapper;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object embeddedProxy = UpdateProxy.create(clazz, mapper.get(clazz), updateBuilder.embedded(field));
        Consumer embeddedDocumentModifier = (Consumer) args[0];
        embeddedDocumentModifier.accept(embeddedProxy);
    }
}
