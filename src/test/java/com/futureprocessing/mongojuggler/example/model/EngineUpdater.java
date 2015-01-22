package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

public interface EngineUpdater {

    @DbField(CarsDBModel.Car.Engine.FUEL)
    EngineUpdater withFuel(String fuel);

    @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
    EngineUpdater withCylindersNumber(int cylindersNumber);
}
