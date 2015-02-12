package com.futureprocessing.mongojuggler.example.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

public interface LuggageUpdater {

    @DbField(CarsDBModel.Car.Luggage.WEIGHT)
    LuggageUpdater withWeight(int weight);

    @DbField(CarsDBModel.Car.Luggage.COLOR)
    LuggageUpdater withColor(String color);
}
