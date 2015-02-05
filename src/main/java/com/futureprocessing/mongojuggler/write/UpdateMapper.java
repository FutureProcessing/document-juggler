package com.futureprocessing.mongojuggler.write;


import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.Push;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.write.command.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class UpdateMapper extends Mapper<UpdateCommand> {

    public static final UpdateMapper INSTANCE = new UpdateMapper();

    private UpdateMapper() {
    }

    @Override
    protected UpdateCommand getCommand(Method method) {
        String field = Metadata.getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            return new EmbeddedUpdateCommand(field, getEmbeddedDocumentType(method));
        }

        if (method.isAnnotationPresent(AddToSet.class)) {
            return new AddToSetUpdateCommand(field);
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
