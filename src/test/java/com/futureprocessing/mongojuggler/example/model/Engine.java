package com.futureprocessing.mongojuggler.example.model;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Unset;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

public interface Engine {


    interface Reader {
        @DbField(CarsDBModel.Car.Engine.FUEL)
        String getFuel();

        @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
        int getCylindersNumber();
    }

    interface Modifier {
        @DbField(CarsDBModel.Car.Engine.FUEL)
        Modifier withFuel(String fuel);

        @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
        Modifier withCylindersNumber(int cylindersNumber);

        @DbField(CarsDBModel.Car.Engine.CYLINDERS_NUMBER)
        @Unset
        void withoutCylindersNumber();
    }

}
