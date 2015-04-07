package com.futureprocessing.documentjuggler.example.cars.model;

import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.query.*;
import com.futureprocessing.documentjuggler.annotation.update.AddToSet;
import com.futureprocessing.documentjuggler.annotation.update.Inc;
import com.futureprocessing.documentjuggler.annotation.update.Push;
import com.futureprocessing.documentjuggler.annotation.update.Unset;
import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.futureprocessing.documentjuggler.update.operators.Update;

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
    @Unset
    Car withoutModel();

    @DbField(CarsDBModel.Car.RELEASE_DATE)
    Car withReleaseDate(Date releaseDate);

    @DbField(CarsDBModel.Car.AUTOMATIC_GEARBOX)
    Car withAutomaticGearbox(boolean automaticGearbox);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    Car withSideNumber(int number);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    @Inc
    Car increaseSideNumber(int value);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    Car withPassengerNames(Set<String> passengerNames);

    @DbField(CarsDBModel.Car.OWNERS)
    Car withOwners(List<String> owners);

    @DbField(CarsDBModel.Car.OWNERS)
    Car withOwners(String... owners);

    @DbField(CarsDBModel.Car.PASSENGERS_NAMES)
    @AddToSet
    Car addPassengerName(String passengerName);

    @DbField(CarsDBModel.Car.OWNERS)
    @Push
    Car addOwner(String owner);

    @DbField(CarsDBModel.Car.ENGINE)
    @DbEmbeddedDocument()
    Car engine(Consumer<Engine> consumer);

    @DbField(CarsDBModel.Car.LUGGAGE)
    @DbEmbeddedDocument()
    Car withLuggage(Consumer<Luggage>... consumers);

    @DbField(CarsDBModel.Car.ROOF_LUGGAGE)
    @DbEmbeddedDocument()
    Car withRoofLuggage(Consumer<Luggage>... consumers);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    @GreaterThan
    Car withSideNumberGreaterThan(int i);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    @GreaterThanEqual
    Car withSideNumberGreaterThanEqual(int i);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    @LessThan
    Car withSideNumberLessThan(int i);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    @LessThanEqual
    Car withSideNumberLessThanEqual(int i);

    @DbField(CarsDBModel.Car.OWNERS)
    @Exists
    Car withOwnersExists(boolean i);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    Car whereSideNumber(Comparison<Integer> sideNumber);

    @DbField(CarsDBModel.Car.OWNERS)
    Car whereOwners(Comparison<String> owners);

    @DbField(CarsDBModel.Car.SIDE_NUMBER)
    @NotEquals
    Car withSideNumberNotEquals(Object i);

    @DbField(CarsDBModel.Car.OWNERS)
    @NotEquals
    Car withOwnersNotEquals(List i);
}
