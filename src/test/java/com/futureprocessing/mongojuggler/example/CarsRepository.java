package com.futureprocessing.mongojuggler.example;


import com.futureprocessing.mongojuggler.MongoDBProvider;
import com.futureprocessing.mongojuggler.Repository;
import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.futureprocessing.mongojuggler.example.model.CarQuery;
import com.futureprocessing.mongojuggler.example.model.CarReader;
import com.futureprocessing.mongojuggler.example.model.CarUpdater;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class CarsRepository extends Repository<CarReader, CarUpdater, CarQuery, Car.Inserter> {

    public CarsRepository(DB db) {
        super(CarReader.class, CarUpdater.class, CarQuery.class, Car.Inserter.class, db.getCollection(CarsDBModel.Car.COLLECTION));
    }
}
