package com.futureprocessing.mongojuggler.example.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

public interface Luggage {


    @DbField(CarsDBModel.Car.Luggage.WEIGHT)
    int getWeight();

    @DbField(CarsDBModel.Car.Luggage.COLOR)
    String getColor();

}
