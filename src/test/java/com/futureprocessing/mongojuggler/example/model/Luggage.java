package com.futureprocessing.mongojuggler.example.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

public interface Luggage {

    interface Read {

        @DbField(CarsDBModel.Car.Luggage.WEIGHT)
        int getWeight();

        @DbField(CarsDBModel.Car.Luggage.COLOR)
        String getColor();
    }

    interface Update {

        @DbField(CarsDBModel.Car.Luggage.WEIGHT)
        Update withWeight(int weight);

        @DbField(CarsDBModel.Car.Luggage.COLOR)
        Update withColor(String color);
    }
}
