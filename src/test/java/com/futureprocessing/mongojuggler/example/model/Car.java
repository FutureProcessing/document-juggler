package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.DbDocument;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

import java.util.Date;
import java.util.List;
import java.util.Set;

@DbDocument(CarsDBModel.Car.COLLECTION)
public interface Car {

    @DbField(CarsDBModel.Car.ID)
    String getId();

    @DbField(CarsDBModel.Car.BRAND)
    String getBrand();

    @DbField(CarsDBModel.Car.MODEL)
    String getModel();

    @DbField(CarsDBModel.Car.RELEASE_DATE)
    Date getReleaseDate();

    @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
    boolean isAutomaticGearbox();

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    Set<String> getPassengersNames();

    @DbField(CarsDBModel.Car.OWNERS)
    List<String> getOwners();

    @DbField(CarsDBModel.Car.ENGINE)
    @DbEmbeddedDocument
    Engine getEngine();
}
