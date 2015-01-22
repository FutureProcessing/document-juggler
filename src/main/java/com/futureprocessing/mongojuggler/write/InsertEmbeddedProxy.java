package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.commons.Metadata;
import com.mongodb.BasicDBObject;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.futureprocessing.mongojuggler.commons.ProxyCreator.newInsertEmbeddedProxy;

public class InsertEmbeddedProxy implements InvocationHandler {

    private final BasicDBObject dbObject;
    private final BasicDBObject parentDBObject;
    private final String embeddedFieldName;

    public InsertEmbeddedProxy(BasicDBObject parentDBObject, String embeddedFieldName) {
        this.parentDBObject = parentDBObject;
        this.embeddedFieldName = embeddedFieldName;
        this.dbObject = new BasicDBObject();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String field = Metadata.getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            Class returnType = method.getReturnType();
            return newInsertEmbeddedProxy(returnType, field, dbObject);
        }

        dbObject.append(field, args[0]);
        return proxy;
    }


    public void done() {
        parentDBObject.append(embeddedFieldName, dbObject);
    }
}
