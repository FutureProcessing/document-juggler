package com.futureprocessing.mongojuggler.example.cars;


import com.futureprocessing.mongojuggler.Repository;
import com.futureprocessing.mongojuggler.example.cars.model.Car;
import com.mongodb.DB;

public class CarsRepository extends Repository<Car> {

    public CarsRepository(DB db) {
        super(db.getCollection(CarsDBModel.Car.COLLECTION), Car.class);
    }
}
