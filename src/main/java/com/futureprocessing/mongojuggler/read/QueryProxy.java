package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class QueryProxy implements InvocationHandler {

    private final QueryBuilder builder = new QueryBuilder();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String field = method.getAnnotation(DbField.class).value();

        if(field.equals("_id")) {
            builder.and(field).is(new ObjectId((String) args[0]));
        } else {
            builder.and(field).is(args[0]);
        }

        return proxy;
    }

    public DBObject toDBObject() {
        return builder.get();
    }
}
