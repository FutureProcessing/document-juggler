package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.write.UpdateBuilder;

import java.util.function.Consumer;

public class EmbeddedUpdateCommand extends AbstractUpdateCommand {

    private final Class clazz;

    public EmbeddedUpdateCommand(String field, Class clazz) {
        super(field);
        this.clazz = clazz;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object embeddedProxy = ProxyCreator.newUpdateProxy(clazz, updateBuilder.embedded(field));
        Consumer embeddedDocumentModifier = (Consumer) args[0];
        embeddedDocumentModifier.accept(embeddedProxy);
    }
}
