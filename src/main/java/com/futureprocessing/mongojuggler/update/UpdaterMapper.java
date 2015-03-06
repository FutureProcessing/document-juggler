package com.futureprocessing.mongojuggler.update;


import com.futureprocessing.mongojuggler.annotation.*;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.update.command.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class UpdaterMapper extends Mapper<UpdateCommand> {

    public UpdaterMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected UpdateCommand getCommand(Method method) {
        String field = Metadata.getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            Class<?> type = method.isVarArgs() ? getEmbeddedListDocumentType(method) : getEmbeddedDocumentType(method);
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

        if (method.isAnnotationPresent(Inc.class)) {
            return new IncrementUpdateCommand(field);
        }

        if (method.isAnnotationPresent(Unset.class)){
            return new UnsetCommand(field);
        }

        Class parameterClass = method.getParameterTypes()[0];
        if (parameterClass.equals(boolean.class) || parameterClass.equals(Boolean.class)) {
            return new BooleanUpdateCommand(field,
                    method.isAnnotationPresent(UnsetIfNull.class),
                    method.isAnnotationPresent(UnsetIfFalse.class));
        }

        return new BasicUpdateCommand(field, method.isAnnotationPresent(UnsetIfNull.class));
    }

    private Class<?> getEmbeddedDocumentType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private Class<?> getEmbeddedListDocumentType(Method method) {
        GenericArrayType type = (GenericArrayType) method.getGenericParameterTypes()[0];
        return (Class<?>) ((ParameterizedType) type.getGenericComponentType()).getActualTypeArguments()[0];
    }
}
