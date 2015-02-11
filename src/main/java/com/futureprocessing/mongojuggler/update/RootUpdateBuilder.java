package com.futureprocessing.mongojuggler.update;


import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;

public class RootUpdateBuilder implements UpdateBuilder {

    private final BasicDBObject document = new BasicDBObject();

    @Override
    public void set(String field, Object value) {
        get("$set").append(field, value);
    }

    @Override
    public void unset(String field) {
        get("$unset").append(field, null);
    }

    @Override
    public void push(String field, Object value) {
        getEach("$push", field).add(value);
    }

    @Override
    public void addToSet(String field, Object value) {
        getEach("$addToSet", field).add(value);
    }

    @Override
    public BasicDBObject getDocument() {
        return document;
    }

    @Override
    public UpdateBuilder embedded(String field) {
        return new EmbeddedUpdateBuilder(this, field);
    }

    private BasicDBObject get(String field) {
        BasicDBObject value = (BasicDBObject) document.get(field);
        if (value == null) {
            value = new BasicDBObject();
            document.put(field, value);
        }
        return value;
    }

    private List getEach(String action, String field) {
        BasicDBObject push = get(action);

        BasicDBObject object = (BasicDBObject) push.get(field);
        if (object == null) {
            object = new BasicDBObject("$each", new ArrayList<>());
            push.put(field, object);
        }

       return (List) object.get("$each");
    }
}
