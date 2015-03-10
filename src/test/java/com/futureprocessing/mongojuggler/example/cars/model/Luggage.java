package com.futureprocessing.mongojuggler.example.cars.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.cars.CarsDBModel;

public interface Luggage {

    @DbField(CarsDBModel.Car.Luggage.WEIGHT)
    int getWeight();

    @DbField(CarsDBModel.Car.Luggage.COLOR)
    String getColor();


    @DbField(CarsDBModel.Car.Luggage.WEIGHT)
    Luggage withWeight(int weight);

    @DbField(CarsDBModel.Car.Luggage.COLOR)
    Luggage withColor(String color);


}
