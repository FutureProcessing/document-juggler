package com.futureprocessing.mongojuggler;


import com.mongodb.DB;

public interface MongoDBProvider {

    DB db();
}
