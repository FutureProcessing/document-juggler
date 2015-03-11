package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.SimpleDBProvider;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.BeforeClass;

public abstract class BaseIntegrationTest {

    private static final String HOST_KEY = "mongodb.host";
    private static final String PORT_KEY = "mongodb.port";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 27017;

    public static final String DB_NAME = "mongo-juggler-test";

    private static MongoClient client;
    private static DB db;

    @BeforeClass
    public static void initMongo() throws Exception {
        client = new MongoClient(getMongoHost(), getMongoPort());
        db = new SimpleDBProvider(client(), DB_NAME).db();
    }

    private static String getMongoHost() {
        String property = System.getProperty(HOST_KEY);
        return property != null ? property : DEFAULT_HOST;
    }

    private static int getMongoPort() {
        String property = System.getProperty(PORT_KEY);
        return property != null ? Integer.parseInt(property) : DEFAULT_PORT;
    }

    public static DB db() {
        return db;
    }

    public static MongoClient client() {
        return client;
    }

    @After
    public void cleanDB() {
        client.dropDatabase(DB_NAME);
    }
}
