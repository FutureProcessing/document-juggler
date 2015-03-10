package com.futureprocessing.mongojuggler.example.cars.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Unset;
import com.futureprocessing.mongojuggler.example.cars.CarsDBModel;

public interface Engine {


    interface Reader {
        @DbField(CarsDBModel.Car.Engine.FUEL)
        String getFuel();

        @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
        int getCylindersNumber();
    }

    interface Updater {
        @DbField(CarsDBModel.Car.Engine.FUEL)
        Updater withFuel(String fuel);

        @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
        Updater withCylindersNumber(int cylindersNumber);

        @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
        @Unset
        void withoutCylindersNumber();
    }

    interface Inserter {
        @DbField(CarsDBModel.Car.Engine.FUEL)
        Inserter withFuel(String fuel);

        @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
        Inserter withCylindersNumber(int cylindersNumber);
    }

}
