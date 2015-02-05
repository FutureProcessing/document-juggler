package com.futureprocessing.mongojuggler.write;


import com.mongodb.BasicDBObject;

public class RootUpdateBuilder implements UpdateBuilder{

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
        get("$push").append(field, value);
    }

    @Override
    public void addToSet(String field, Object value) {
        get("$addToSet").append(field, value);
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
        if(value == null) {
            value = new BasicDBObject();
            document.put(field, value);
        }
        return value;
    }
}
