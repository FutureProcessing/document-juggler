package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbDocument;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@DbDocument(CarsDBModel.Car.COLLECTION)
public interface CarUpdater {

    @DbField(CarsDBModel.Car.ID)
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
    CarUpdater withPassengerNames(List<String> passengerNames);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    @AddToSet
    CarUpdater addPassengerName(String passengerName);

    @DbField(CarsDBModel.Car.ENGINE)
    @DbEmbeddedDocument(EngineUpdater.class)
    CarUpdater engine(Consumer<EngineUpdater> consumer);

}
