package com.futureprocessing.mongojuggler.example;


import com.futureprocessing.mongojuggler.Repository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.mongodb.DB;

public class CarsRepository extends Repository<Car.Reader, Car.Updater, Car.Querier, Car.Inserter> {

    public CarsRepository(DB db) {
        super(Car.Reader.class, Car.Updater.class, Car.Querier.class, Car.Inserter.class,
              db.getCollection(CarsDBModel.Car.COLLECTION));
    }
}
