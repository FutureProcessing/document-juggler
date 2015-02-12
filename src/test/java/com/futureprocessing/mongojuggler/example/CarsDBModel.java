package com.futureprocessing.mongojuggler.example;


public interface CarsDBModel {

    static interface Car {

        String COLLECTION = "Cars";
        String ID = "_id";
        String BRAND = "brand";
        String MODEL = "model";
        String RELEASE_DATE = "releaseDate";
        String AUTOMATIC_GEARBOX = "automaticGearbox";
        String ENGINE = "engine";
        String PASSENGERS_NAMES = "passengersNames";
        String OWNERS = "owners";
        String LUGGAGE = "luggage";

        static interface Engine {
            String FUEL = "fuel";
            String CYLINDERS_NUMBER = "cylindersNumber";
        }

        static interface Luggage {
            String WEIGHT = "weight";
            String COLOR = "color";
        }
    }


}
