package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdateProxy;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static com.futureprocessing.documentjuggler.commons.ArgumentsReader.from;
import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class EmbeddedUpdateCommand extends AbstractUpdateCommand {

    private final Class clazz;
    private final Mapper<UpdateCommand> mapper;

    public EmbeddedUpdateCommand(String field, Class clazz, Mapper<UpdateCommand> mapper) {
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

    public static class Provider implements CommandProvider<UpdateCommand> {

        @Override
        public UpdateCommand getCommand(Method method, Mapper<UpdateCommand> mapper) {
            Class<?> type = from(method).getGenericType(0); // TODO Move to Embedded Update command
            mapper.createEmbeddedMapping(getFieldName(method), type);
            return new EmbeddedUpdateCommand(getFieldName(method), type, mapper);
        }

    }
}
