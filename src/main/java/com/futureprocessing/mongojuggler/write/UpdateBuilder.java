package com.futureprocessing.mongojuggler.write;


import com.mongodb.BasicDBObject;

public interface UpdateBuilder {

    void set(String field, Object value);

    void unset(String field);

    void push(String field, Object value);

    void addToSet(String field, Object value);

    BasicDBObject getDocument();

    UpdateBuilder embedded(String field);
}
