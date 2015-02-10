package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.write.UpdateBuilder;
import com.futureprocessing.mongojuggler.write.UpdateMapper;

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
        Object embeddedProxy = ProxyCreator.newUpdateProxy(clazz, mapper, updateBuilder.embedded(field));
        Consumer embeddedDocumentModifier = (Consumer) args[0];
        embeddedDocumentModifier.accept(embeddedProxy);
    }
}
