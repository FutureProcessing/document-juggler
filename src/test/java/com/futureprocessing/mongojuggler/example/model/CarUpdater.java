package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.*;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@DbDocument(CarsDBModel.Car.COLLECTION)
public interface CarUpdater {

    @Id
    CarUpdater withId(String id);

    @DbField(CarsDBModel.Car.BRAND)
    CarUpdater withBrand(String brand);

    @DbField(CarsDBModel.Car.MODEL)
    CarUpdater withModel(String model);

    @DbField(CarsDBModel.Car.RELEASE_DATE)
    CarUpdater withReleaseDate(Date releaseDate);

    @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
    CarUpdater withAutomaticGearbox(boolean automaticGearbox);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    CarUpdater withPassengerNames(Set<String> passengerNames);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    @AddToSet
    CarUpdater addPassengerName(String passengerName);

    @DbField(CarsDBModel.Car.ENGINE)
    @DbEmbeddedDocument(EngineUpdater.class)
    CarUpdater engine(Consumer<EngineUpdater> consumer);

}
