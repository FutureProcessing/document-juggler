package com.futureprocessing.documentjuggler.update;


import com.mongodb.BasicDBObject;

public class EmbeddedUpdateBuilder implements UpdateBuilder {

    private UpdateBuilder updateBuilder;
    private final String field;

    public EmbeddedUpdateBuilder(UpdateBuilder updateBuilder, String field) {
        this.updateBuilder = updateBuilder;
        this.field = field;
    }

    @Override
    public void set(String field, Object value) {
        updateBuilder.set(fullPath(field), value);
    }

    @Override
    public void unset(String field) {
        updateBuilder.unset(fullPath(field));
    }

    @Override
    public void push(String field, Object value) {
        updateBuilder.push(fullPath(field), value);
    }

    @Override
    public void addToSet(String field, Object value) {
        updateBuilder.addToSet(fullPath(field), value);
    }

    @Override
    public BasicDBObject getDocument() {
        return updateBuilder.getDocument();
    }

    @Override
    public UpdateBuilder embedded(String field) {
        return new EmbeddedUpdateBuilder(this, field);
    }

    @Override
    public void inc(String field, Object value) {
        updateBuilder.inc(fullPath(field), value);
    }

    private String fullPath(String embeddedField) {
        return field + "." + embeddedField;
    }
}
