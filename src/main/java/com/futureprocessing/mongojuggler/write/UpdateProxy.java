package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.Push;
import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public class UpdateProxy implements InvocationHandler {

    private final DBUpdateQueryBuilder updateQueryBuilder;
    private final DBCollection collection;
    private final DBObject query;

    public UpdateProxy(DBCollection collection, DBObject query) {
        this.collection = collection;
        this.query = query;
        this.updateQueryBuilder = new DBUpdateQueryBuilder();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String field = Metadata.getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            Class<?> embeddedDocumentType = getEmbeddedDocumentType(method);

            Object embeddedProxy = ProxyCreator.newUpdateEmbeddedProxy(embeddedDocumentType, this, field);
            Consumer embeddedDocumentModifier = (Consumer) args[0];
            embeddedDocumentModifier.accept(embeddedProxy);
            return proxy;
        }

        if (method.isAnnotationPresent(AddToSet.class)) {
            addToSet(field, args);
            return proxy;
        }

        if (method.isAnnotationPresent(Push.class)) {
            pushToList(field, args);
            return proxy;
        }

        setFieldValue(method, field, args);

        return proxy;
    }

    private Class<?> getEmbeddedDocumentType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private void pushToList(String field, Object[] args) {
        final Object value = args[0];
        updateQueryBuilder.push(field, value);
    }

    private void addToSet(String field, Object[] args) {
        final Object value = args[0];
        updateQueryBuilder.addToSet(field, value);
    }

    public void setFieldValue(Method method, String field, Object[] args) {
        final Object value = args[0];

        if (value != null) {
            if (parameterIsBoolean(method)) {
                updateQueryBuilder.setBooleanField(field, (Boolean) value);
            } else {
                updateQueryBuilder.setField(field, value);
            }
        } else {
            updateQueryBuilder.unsetField(field);
        }
    }

    private boolean parameterIsBoolean(Method method) {
        return method.getParameterTypes()[0].equals(boolean.class)
                || method.getParameterTypes()[0].equals(Boolean.class);
    }

    public WriteResult execute() {
        updateQueryBuilder.validate();
        return collection.update(query, updateQueryBuilder.getValue());
    }
}
