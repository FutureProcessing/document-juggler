package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CarReader {

    @DbField(CarsDBModel.Car.ID)
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
    Engine getEngine();

    @DbField(CarsDBModel.Car.LUGGAGE)
    @DbEmbeddedDocument
    List<Luggage> getLuggage();

    @DbField(CarsDBModel.Car.ROOF_LUGGAGE)
    @DbEmbeddedDocument
    Set<Luggage> getRoofLuggage();
}
