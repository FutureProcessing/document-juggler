package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class IncrementUpdateCommand extends AbstractUpdateCommand {

    public IncrementUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.inc(field, (Integer) args[0]);
    }

    public static class Provider implements CommandProvider<UpdateCommand> {

        @Override
        public UpdateCommand getCommand(Method method, Mapper<UpdateCommand> mapper) {
            return new IncrementUpdateCommand(getFieldName(method));
        }
    }
}
