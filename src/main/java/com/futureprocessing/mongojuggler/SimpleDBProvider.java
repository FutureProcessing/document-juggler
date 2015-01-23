package com.futureprocessing.mongojuggler;


import com.mongodb.DB;
import com.mongodb.MongoClient;

public class SimpleDBProvider implements MongoDBProvider {

    private final MongoClient client;
    private final String dbName;

    public SimpleDBProvider(MongoClient client, String dbName) {
        this.client = client;
        this.dbName = dbName;
    }

    @Override
    public DB db() {
        return client.getDB(dbName);
    }
}
