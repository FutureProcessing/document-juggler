package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.exception.UnsupportedActionException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import static com.futureprocessing.mongojuggler.commons.ProxyExtractor.extractInsertEmbeddedProxy;

public class InsertProxy implements InvocationHandler {

    private final DBCollection collection;
    private final BasicDBObject dbObject;

    public InsertProxy(DBCollection collection) {
        this.collection = collection;
        this.dbObject = new BasicDBObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String field = Metadata.getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            Class<?> type = method.getAnnotation(DbEmbeddedDocument.class).value();
            Object embedded = ProxyCreator.newInsertEmbeddedProxy(type, field, dbObject);
            Consumer consumer = (Consumer) args[0];
            consumer.accept(embedded);
            extractInsertEmbeddedProxy(embedded).done();
            return proxy;
        }

        if (method.isAnnotationPresent(AddToSet.class)) {
            throw new UnsupportedActionException();
        }

        if (field.equals("_id")) {
            dbObject.append(field, new ObjectId((String) args[0]));
        } else {
            dbObject.append(field, args[0]);
        }
        return proxy;
    }

    public String execute() {
        collection.insert(dbObject);
        return ((ObjectId) dbObject.get("_id")).toHexString();
    }

}
