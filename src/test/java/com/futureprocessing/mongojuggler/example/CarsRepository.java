package com.futureprocessing.mongojuggler.example;


import com.futureprocessing.mongojuggler.MongoDBProvider;
import com.futureprocessing.mongojuggler.Repository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.futureprocessing.mongojuggler.example.model.CarQuery;
import com.futureprocessing.mongojuggler.example.model.CarReader;
import com.futureprocessing.mongojuggler.example.model.CarUpdater;

public class CarsRepository extends Repository<CarReader, CarUpdater, CarQuery, Car.Inserter> {

    public CarsRepository(MongoDBProvider dbProvider) {
        super(CarReader.class, CarUpdater.class, CarQuery.class, Car.Inserter.class, dbProvider);
    }
}
