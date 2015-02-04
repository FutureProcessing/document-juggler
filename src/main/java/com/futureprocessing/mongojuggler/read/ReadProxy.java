package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.futureprocessing.mongojuggler.read.command.ReadCommand;
import com.mongodb.BasicDBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;


public class ReadProxy implements InvocationHandler {

    private final BasicDBObject dbObject;
    private final Set<String> queriedFields;
    private final Map<Method, ReadCommand> readCommands;

    public ReadProxy(Class<?> clazz, BasicDBObject dbObject, Set<String> queriedFields) {
        if (dbObject == null) {
            throw new RuntimeException("Null dbObject");
        }
        this.dbObject = dbObject;
        this.queriedFields = queriedFields;
        readCommands = ReadMapper.get(clazz);
    }

    @Override
    public Object invoke(Object object, Method method, Object[] params) throws Throwable {
        String field = getFieldName(method);
        if (queriedFields.isEmpty() || queriedFields.contains(field)) {
            return readCommands.get(method).read(dbObject);
        }

        throw new FieldNotLoadedException(field);
    }

    private String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }
}
