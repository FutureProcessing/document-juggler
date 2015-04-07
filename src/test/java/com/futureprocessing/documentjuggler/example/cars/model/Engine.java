package com.futureprocessing.documentjuggler.example.cars.model;


import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.update.operators.Update;

public interface Engine {

    @DbField(CarsDBModel.Car.Engine.FUEL)
    String getFuel();

    @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
    int getCylindersNumber();

    @DbField(CarsDBModel.Car.Engine.FUEL)
    Engine withFuel(String fuel);

    @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
    Engine withCylindersNumber(int cylindersNumber);

    @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
    void withCylindersNumber(Update<Integer> cylindersNumber);

}
