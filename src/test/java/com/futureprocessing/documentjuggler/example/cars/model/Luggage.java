package com.futureprocessing.documentjuggler.example.cars.model;


import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;

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
