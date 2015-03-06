package com.futureprocessing.mongojuggler.example.cars.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.cars.CarsDBModel;

public interface Luggage {

    interface Reader {
        @DbField(CarsDBModel.Car.Luggage.WEIGHT)
        int getWeight();

        @DbField(CarsDBModel.Car.Luggage.COLOR)
        String getColor();
    }

    interface Modifier {
        @DbField(CarsDBModel.Car.Luggage.WEIGHT)
        Modifier withWeight(int weight);

        @DbField(CarsDBModel.Car.Luggage.COLOR)
        Modifier withColor(String color);
    }


}
