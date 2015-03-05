package com.futureprocessing.mongojuggler.example;


import com.futureprocessing.mongojuggler.Repository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.mongodb.DB;

public class CarsRepository extends Repository<Car.Inserter, Car.Querier, Car.Reader, Car.Updater> {

    public CarsRepository(DB db) {
        super(db.getCollection(CarsDBModel.Car.COLLECTION),
                Car.Inserter.class,
                Car.Querier.class,
                Car.Reader.class,
                Car.Updater.class);
    }
}
