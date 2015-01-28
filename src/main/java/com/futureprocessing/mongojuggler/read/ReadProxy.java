package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;


public class ReadProxy implements InvocationHandler {

    private final BasicDBObject dbObject;
    private final Set<String> queriedFields;

    public ReadProxy(BasicDBObject dbObject, Set<String> queriedFields) {
        if (dbObject == null) {
            throw new RuntimeException("Null dbObject");
        }
        this.dbObject = dbObject;
        this.queriedFields = queriedFields;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] params) throws Throwable {
        String field = getFieldName(method);
        if (queriedFields.isEmpty() || queriedFields.contains(field)) {
            if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
                return ProxyCreator.newReadProxy(method.getReturnType(), (BasicDBObject) dbObject.get(field), unmodifiableSet(emptySet()));
            }

            if (isBooleanReturnType(method)) {
                return dbObject.getBoolean(field);
            }

            if(field.equals("_id")) {
                return ((ObjectId)dbObject.get(field)).toHexString();
            }
            return dbObject.get(field);
        }

        throw new FieldNotLoadedException(field);
    }

    private boolean isBooleanReturnType(Method method) {
        return method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class);
    }


    private String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }
}
