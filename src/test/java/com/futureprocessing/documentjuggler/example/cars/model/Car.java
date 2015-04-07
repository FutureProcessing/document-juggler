package com.futureprocessing.documentjuggler.example.cars.model;

import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.query.Exists;
import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.futureprocessing.documentjuggler.update.operators.Update;
import com.futureprocessing.documentjuggler.update.operators.UpdateArrays;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@CollectionName(CarsDBModel.Car.COLLECTION)
public interface Car {
    @AsObjectId
    @DbField(CarsDBModel.Car.ID)
    Car withId(String id);

    @AsObjectId
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

    @DbField(CarsDBModel.Car.BRAND)
    Car withBrand(String brand);

    @DbField(CarsDBModel.Car.MODEL)
    Car withModel(String model);

    @DbField(CarsDBModel.Car.MODEL)
    Car withModel(Update<String> model);

    @DbField(CarsDBModel.Car.RELEASE_DATE)
    Car withReleaseDate(Date releaseDate);

    @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
    Car withAutomaticGearbox(boolean automaticGearbox);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    Car withSideNumber(int number);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    Car withSideNumber(Update<Integer> number);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    Car withPassengerNames(Set<String> passengerNames);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    Car withPassengerNames(UpdateArrays<String> passengerNames);

    @DbField(CarsDBModel.Car.OWNERS)
    Car withOwners(List<String> owners);

    @DbField(CarsDBModel.Car.OWNERS)
    Car withOwners(String... owners);

    @DbField(CarsDBModel.Car.OWNERS)
    Car withOwners(UpdateArrays<String> owners);

    @DbField(CarsDBModel.Car.ENGINE)
    @DbEmbeddedDocument()
    Car engine(Consumer<Engine> consumer);

    @DbField(CarsDBModel.Car.LUGGAGE)
    @DbEmbeddedDocument()
    Car withLuggage(Consumer<Luggage>... consumers);

    @DbField(CarsDBModel.Car.ROOF_LUGGAGE)
    @DbEmbeddedDocument()
    Car withRoofLuggage(Consumer<Luggage>... consumers);

    @DbField(CarsDBModel.Car.OWNERS)
    @Exists
    Car withOwnersExists(boolean i);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    Car whereSideNumber(Comparison<Integer> sideNumber);

    @DbField(CarsDBModel.Car.OWNERS)
    Car whereOwners(Comparison<String> owners);
}
