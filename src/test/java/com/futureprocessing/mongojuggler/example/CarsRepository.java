package com.futureprocessing.mongojuggler.example;


import com.futureprocessing.mongojuggler.MongoDBProvider;
import com.futureprocessing.mongojuggler.example.model.CarUpdater;
import com.futureprocessing.mongojuggler.Repository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.futureprocessing.mongojuggler.example.model.CarQuery;

public class CarsRepository extends Repository<Car, CarUpdater, CarQuery> {

    public CarsRepository(MongoDBProvider dbProvider) {
        super(Car.class, CarUpdater.class, CarQuery.class, dbProvider);
    }
}
