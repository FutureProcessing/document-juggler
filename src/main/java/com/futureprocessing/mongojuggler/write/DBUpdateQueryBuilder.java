package com.futureprocessing.mongojuggler.write;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.futureprocessing.mongojuggler.exception.MissingPropertyException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBUpdateQueryBuilder {

    protected final Map<String, Object> setObjects = new LinkedHashMap<>();
    protected final List<String> unsetObjects = new ArrayList<>();
    protected final Map<String, Object> addToSetObjects = new LinkedHashMap<>();

    public DBObject getValue() {
        BasicDBObject value = new BasicDBObject();

        if (!setObjects.isEmpty()) {
            value.append("$set", getSetObject());
        }

        if (!unsetObjects.isEmpty()) {
            value.append("$unset", getUnsetObject());
        }

        if (!addToSetObjects.isEmpty()){
            appendAddToSetAbjects(value);
        }

        return value;
    }

    private void appendAddToSetAbjects(BasicDBObject value) {
        addToSetObjects.entrySet().stream().forEach(
                entry -> value.append("$addToSet", new BasicDBObject(entry.getKey(), entry.getValue())));
    }

    private DBObject getSetObject() {
        BasicDBObject object = new BasicDBObject();

        for (Map.Entry<String, Object> entry : setObjects.entrySet()) {
            object.append(entry.getKey(), entry.getValue());
        }

        return object;
    }

    private DBObject getUnsetObject() {
        BasicDBObject object = new BasicDBObject();

        for (String key : unsetObjects) {
            object.append(key, null);
        }

        return object;
    }


    public DBUpdateQueryBuilder setField(String key, Object value) {
        setObjects.put(key, value);
        return this;
    }

    public DBUpdateQueryBuilder unsetField(String key) {
        unsetObjects.add(key);
        return this;
    }

    public void validate() {
        if (setObjects.isEmpty() && unsetObjects.isEmpty() && addToSetObjects.isEmpty()) {
            throw new MissingPropertyException("No property to update specified");
        }
    }

    public DBUpdateQueryBuilder setBooleanField(String fieldName, Boolean boolValue) {
        if (boolValue == null) {
            return this;
        }

        if (boolValue) {
            setField(fieldName, true);
        } else {
            unsetField(fieldName);
        }
        return this;
    }

    public void addToSet(String field, Object value) {
        this.addToSetObjects.put(field, value);
    }
}
