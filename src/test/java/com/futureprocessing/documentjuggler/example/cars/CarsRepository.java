package com.futureprocessing.documentjuggler.example.cars;


import com.futureprocessing.documentjuggler.Repository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import com.mongodb.DB;

public class CarsRepository extends Repository<Car> {

    public CarsRepository(DB db) {
        super(db.getCollection(CarsDBModel.Car.COLLECTION), Car.class);
    }
}
