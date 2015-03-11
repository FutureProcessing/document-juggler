package com.futureprocessing.mongojuggler.example.cars.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Unset;
import com.futureprocessing.mongojuggler.example.cars.CarsDBModel;

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
    @Unset
    void withoutCylindersNumber();

}
