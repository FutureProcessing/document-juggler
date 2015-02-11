package com.futureprocessing.mongojuggler.update;


import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.Push;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.update.command.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class UpdateMapper extends Mapper<UpdateCommand> {

    public UpdateMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected UpdateCommand getCommand(Method method) {
        String field = Metadata.getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            Class<?> type = getEmbeddedDocumentType(method);
            createMapping(type);
            return new EmbeddedUpdateCommand(field, type, this);
        }

        if (method.isAnnotationPresent(AddToSet.class)) {
            if (Collection.class.isAssignableFrom(method.getParameterTypes()[0])) {
                return new AddToSetCollectionUpdateCommand(field);
            }
            if (method.getParameterCount() > 1) {
                return new AddToSetManyUpdateCommand(field);
            }
            if (method.isVarArgs() || method.getParameterTypes()[0].isArray()) {
                return new AddToSetArrayUpdateCommand(field);
            }
            return new AddToSetSingleUpdateCommand(field);
        }

        if (method.isAnnotationPresent(Push.class)) {
            return new PushUpdateCommand(field);
        }

        Class parameterClass = method.getParameterTypes()[0];
        if (parameterClass.equals(boolean.class) || parameterClass.equals(Boolean.class)) {
            return new BooleanUpdateCommand(field);
        }

        return new BasicUpdateCommand(field);
    }

    private Class<?> getEmbeddedDocumentType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }
}
