package com.futureprocessing.mongojuggler.write;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.futureprocessing.mongojuggler.exception.DocumentNotFoundException;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class DBUpdater {

    private static final Logger LOG = getLogger(DBUpdater.class);

    private final DBCollection dbCollection;
    private final DBObject query;

    private DBUpdateQueryBuilder queryBuilder = new DBUpdateQueryBuilder();

    public DBUpdater(DBCollection dbCollection, DBObject query) {
        this.dbCollection = dbCollection;
        this.query = query;
    }

    public void execute() {
        WriteResult writeResult = executeUpdate();
        processWriteResult(writeResult);
    }

    protected void processWriteResult(WriteResult writeResult) {
        if (writeResult.getN() == 0) {
            LOG.warn("Document not found in collection {} for query {}", dbCollection, getQuery());
            throw new DocumentNotFoundException();
        }
    }

    private WriteResult executeUpdate() {
        queryBuilder.validate();
        return dbCollection.update(getQuery(), queryBuilder.getValue());
    }

    protected DBObject getQuery(){
        return this.query;
    }

    public DBUpdater setField(String key, Object value) {
        queryBuilder.setField(key, value);
        return this;
    }

    public DBUpdater unsetField(String key) {
        queryBuilder.unsetField(key);
        return this;
    }

    public DBUpdater setBooleanField(String fieldName, Boolean boolValue) {
        queryBuilder.setBooleanField(fieldName, boolValue);
        return this;
    }

    public void addToSet(String field, Object value) {
        queryBuilder.addToSet(field, value);
    }
}
