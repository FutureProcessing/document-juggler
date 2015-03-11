package com.futureprocessing.documentjuggler;


import com.mongodb.DB;

public interface MongoDBProvider {

    DB db();
}
