package com.futureprocessing.documentjuggler.update.command.providers;

import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.*;

import java.lang.reflect.Method;
import java.util.Collection;

import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class PushCommandProvider implements CommandProvider<UpdateCommand> {
    @Override
    public UpdateCommand getCommand(Method method, Mapper<UpdateCommand> mapper) {
        final String field = getFieldName(method);

        if (Collection.class.isAssignableFrom(method.getParameterTypes()[0])) {
            return new PushCollectionUpdateCommand(field);
        }
        if (method.getParameterCount() > 1) {
            return new PushManyUpdateCommand(field);
        }
        if (method.isVarArgs() || method.getParameterTypes()[0].isArray()) {
            return new PushArrayUpdateCommand(field);
        }
        return new PushSingleUpdateCommand(field);
    }
}
