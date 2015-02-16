package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.*;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@DbDocument(CarsDBModel.Car.COLLECTION)
public interface CarUpdater {

    @DbField(CarsDBModel.Car.BRAND)
    CarUpdater withBrand(String brand);

    @DbField(CarsDBModel.Car.MODEL)
    CarUpdater withModel(String model);

    @DbField(CarsDBModel.Car.RELEASE_DATE)
    CarUpdater withReleaseDate(Date releaseDate);

    @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
    CarUpdater withAutomaticGearbox(boolean automaticGearbox);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    CarUpdater withSideNumber(int number);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    @Inc
    CarUpdater increaseSideNumber(int value);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    CarUpdater withPassengerNames(Set<String> passengerNames);

    @DbField(CarsDBModel.Car.OWNERS)
    CarUpdater withOwners(List<String> owners);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    @AddToSet
    CarUpdater addPassengerName(String passengerName);

    @DbField(CarsDBModel.Car.OWNERS)
    @Push
    CarUpdater addOwner(String owner);

    @DbField(CarsDBModel.Car.ENGINE)
    @DbEmbeddedDocument()
    CarUpdater engine(Consumer<EngineUpdater> consumer);

    @DbField(CarsDBModel.Car.LUGGAGE)
    @DbEmbeddedDocument()
    CarUpdater withLuggage(Consumer<LuggageUpdater>... consumers);

    @DbField(CarsDBModel.Car.ROOF_LUGGAGE)
    @DbEmbeddedDocument()
    CarUpdater withRoofLuggage(Consumer<LuggageUpdater>... consumers);

}
