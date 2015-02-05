package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.write.command.InsertCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class InsertProxy implements InvocationHandler {

    private final DBCollection collection;
    private final BasicDBObject dbObject;
    private final Map<Method, InsertCommand> insertCommands;

    public InsertProxy(Class<?> clazz, DBCollection collection) {
        this.collection = collection;
        this.dbObject = new BasicDBObject();
        insertCommands = InsertMapper.get(clazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        insertCommands.get(method).insert(dbObject, args);
        return proxy;
    }

    public String execute() {
        collection.insert(dbObject);
        return ((ObjectId) dbObject.get("_id")).toHexString();
    }

}
