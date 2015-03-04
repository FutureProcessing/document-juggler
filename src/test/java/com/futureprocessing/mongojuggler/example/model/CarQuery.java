package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

public interface CarQuery {

    @Id
    CarQuery withId(String id);

    @DbField(CarsDBModel.Car.BRAND)
    CarQuery withBrand(String brand);
}
