package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.annotation.*;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static com.futureprocessing.documentjuggler.Context.UPDATE;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public class UpdateMapper extends Mapper<UpdateCommand> {

    public UpdateMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected UpdateCommand getCommand(Method method) {
        AnnotationReader annotationReader = from(method);
        String field = FieldNameExtractor.getFieldName(method);

        if (isForbidden(method, UPDATE) || !hasCorrectReturnType(method) || field.equals("_id")) {
            return new ForbiddenUpdateCommand(method);
        }

        if (annotationReader.isPresent(DbEmbeddedDocument.class)) {
            Class<?> type = method.isVarArgs() ? getEmbeddedListDocumentType(method) : getEmbeddedDocumentType(method);
            createMapping(type);
            return new EmbeddedUpdateCommand(field, type, this);
        }

        if (annotationReader.isPresent(AddToSet.class)) {
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

        if (annotationReader.isPresent(Push.class)) {
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

        if (annotationReader.isPresent(Inc.class)) {
            return new IncrementUpdateCommand(field);
        }

        if (method.isAnnotationPresent(Unset.class)) {
            return new UnsetCommand(field);
        }

        Class parameterClass = method.getParameterTypes()[0];
        if (parameterClass.equals(boolean.class) || parameterClass.equals(Boolean.class)) {
            return new BooleanUpdateCommand(field,
                    annotationReader.isPresent(UnsetIfNull.class),
                    annotationReader.isPresent(UnsetIfFalse.class));
        }

        return new BasicUpdateCommand(field, annotationReader.isPresent(UnsetIfNull.class));
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        return returnType.equals(clazz) || returnType.equals(Void.TYPE);
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
