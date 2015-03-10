package com.futureprocessing.mongojuggler.example.cars.model;

import com.futureprocessing.mongojuggler.annotation.*;
import com.futureprocessing.mongojuggler.example.cars.CarsDBModel;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface Car {

    interface Inserter {
        @DbField(CarsDBModel.Car.BRAND)
        Inserter withBrand(String brand);

        @DbField(CarsDBModel.Car.MODEL)
        Inserter withModel(String model);

        @DbField(CarsDBModel.Car.RELEASE_DATE)
        Inserter withReleaseDate(Date releaseDate);

        @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
        Inserter withAutomaticGearbox(boolean automaticGearbox);

        @DbField(CarsDBModel.Car.SIDE_NUMBER)
        Inserter withSideNumber(int number);

        @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
        Inserter withPassengerNames(Set<String> passengerNames);

        @DbField(CarsDBModel.Car.OWNERS)
        Inserter withOwners(List<String> owners);

        @DbField(CarsDBModel.Car.ENGINE)
        @DbEmbeddedDocument()
        Inserter engine(Consumer<Engine.Modifier> consumer);

        @DbField(CarsDBModel.Car.LUGGAGE)
        @DbEmbeddedDocument()
        Inserter withLuggage(Consumer<Luggage.Modifier>... consumers);

        @DbField(CarsDBModel.Car.ROOF_LUGGAGE)
        @DbEmbeddedDocument()
        Inserter withRoofLuggage(Consumer<Luggage.Modifier>... consumers);
    }

    interface Querier {
        @Id
        Querier withId(String id);

        @DbField(CarsDBModel.Car.BRAND)
        Querier withBrand(String brand);
    }

    interface Reader {

        @DbField(CarsDBModel.Car.ID) //todo return ObjectId, use @Id for id as String
        String getId();

        @DbField(CarsDBModel.Car.BRAND)
        String getBrand();

        @DbField(CarsDBModel.Car.MODEL)
        String getModel();

        @DbField(CarsDBModel.Car.RELEASE_DATE)
        Date getReleaseDate();

        @DbField(CarsDBModel.Car.SIDE_NUMBER)
        int getSideNumber();

        @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
        boolean isAutomaticGearbox();

        @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
        Set<String> getPassengersNames();

        @DbField(CarsDBModel.Car.OWNERS)
        List<String> getOwners();

        @DbField(CarsDBModel.Car.ENGINE)
        @DbEmbeddedDocument
        Engine.Reader getEngine();

        @DbField(CarsDBModel.Car.LUGGAGE)
        @DbEmbeddedDocument
        List<Luggage.Reader> getLuggage();

        @DbField(CarsDBModel.Car.ROOF_LUGGAGE)
        @DbEmbeddedDocument
        Set<Luggage.Reader> getRoofLuggage();
    }

    interface Updater {

        @DbField(CarsDBModel.Car.BRAND)
        Updater withBrand(String brand);

        @DbField(CarsDBModel.Car.MODEL)
        Updater withModel(String model);

        @DbField(CarsDBModel.Car.MODEL)
        @Unset
        Updater withoutModel();

        @DbField(CarsDBModel.Car.RELEASE_DATE)
        Updater withReleaseDate(Date releaseDate);

        @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
        Updater withAutomaticGearbox(boolean automaticGearbox);

        @DbField(CarsDBModel.Car.SIDE_NUMBER)
        Updater withSideNumber(int number);

        @DbField(CarsDBModel.Car.SIDE_NUMBER)
        @Inc
        Updater increaseSideNumber(int value);

        @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
        Updater withPassengerNames(Set<String> passengerNames);

        @DbField(CarsDBModel.Car.OWNERS)
        Updater withOwners(List<String> owners);

        @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
        @AddToSet
        Updater addPassengerName(String passengerName);

        @DbField(CarsDBModel.Car.OWNERS)
        @Push
        Updater addOwner(String owner);

        @DbField(CarsDBModel.Car.ENGINE)
        @DbEmbeddedDocument()
        Updater engine(Consumer<Engine.Modifier> consumer);

        @DbField(CarsDBModel.Car.LUGGAGE)
        @DbEmbeddedDocument()
        Updater withLuggage(Consumer<Luggage.Modifier>... consumers);

        @DbField(CarsDBModel.Car.ROOF_LUGGAGE)
        @DbEmbeddedDocument()
        Updater withRoofLuggage(Consumer<Luggage.Modifier>... consumers);

    }
}
