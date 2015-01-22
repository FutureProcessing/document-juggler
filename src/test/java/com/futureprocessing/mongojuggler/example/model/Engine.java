package com.futureprocessing.mongojuggler.example.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

public interface Engine {

    @DbField(CarsDBModel.Car.Engine.FUEL)
    String getFuel();

    @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
    int getCylindersNumber();
}
