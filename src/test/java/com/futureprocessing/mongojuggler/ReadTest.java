package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.futureprocessing.mongojuggler.example.model.Engine;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReadTest {

    public static final String PROPER_ID = new ObjectId().toHexString();
    private CarsRepository carsRepository;

    @Mock
    private DB db;
    @Mock
    private DBCollection collection;
    @Mock
    private MongoDBProvider dbProvider;


    @Before
    public void before() {
        given(dbProvider.db()).willReturn(db);
        given(db.getCollection(any())).willReturn(collection);

        carsRepository = new CarsRepository(dbProvider);
    }

    @Test
    public void shouldFetchDataFromGivenCollection() {
        // given
        given(db.getCollection(any())).willReturn(null);
        given(db.getCollection(CarsDBModel.Car.COLLECTION)).willReturn(collection);

        final String brand = "Fiat";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(BRAND);

        // then
        assertThat(entity.getBrand()).isEqualTo(brand);
    }


    @Test
    public void shouldQueryForObjectWithId() {
        // given
        given(collection.findOne(any(), any())).willReturn(new BasicDBObject());
        DBObject expectedQuery = new BasicDBObject(ID, new ObjectId(PROPER_ID));

        // when
        carsRepository.find(car -> car.withId(PROPER_ID)).first();

        // then
        verify(collection).findOne(eq(expectedQuery), any());
    }

}
