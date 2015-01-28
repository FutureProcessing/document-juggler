package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.DbDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;

@DbDocument(CarsDBModel.Car.COLLECTION)
public interface CarQuery {

    @DbField("_id")
    CarQuery withId(String id);

    @DbField(CarsDBModel.Car.BRAND)
    CarQuery withBrand(String brand);
}
