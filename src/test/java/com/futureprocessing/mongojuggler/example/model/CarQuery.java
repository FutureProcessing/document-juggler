package com.futureprocessing.mongojuggler.example.model;

import com.futureprocessing.mongojuggler.annotation.DbDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.read.AbstractQuery;

@DbDocument(CarsDBModel.Car.COLLECTION)
public interface CarQuery extends AbstractQuery<CarQuery> {

    @DbField(CarsDBModel.Car.BRAND)
    CarQuery withBrand(String brand);
}
