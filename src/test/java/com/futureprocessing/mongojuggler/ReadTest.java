package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.CarReader;
import com.futureprocessing.mongojuggler.example.model.Engine;
import com.futureprocessing.mongojuggler.example.model.Luggage;
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
import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.Engine.CYLINDERS_NUMBER;
import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.Engine.FUEL;
import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.Luggage.COLOR;
import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.Luggage.WEIGHT;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
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
        CarReader entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(BRAND);

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

    @Test
    public void shouldCacheEmbeddedDoc() {
        // given
        BasicDBObject model = new BasicDBObject(ENGINE,
                new BasicDBObject(CYLINDERS_NUMBER, 12).append(FUEL, "gas")
        );
        given(collection.findOne(any(), any())).willReturn(model);

        CarReader carReader = carsRepository.find(c -> c.withId(PROPER_ID)).first();

        // when
        Engine engine1 = carReader.getEngine();
        Engine engine2 = carReader.getEngine();

        // then
        assertThat(engine2).isSameAs(engine1);
    }

    @Test
    public void shouldCacheListOfEmbeddedDocs() {
        // given
        BasicDBObject model = new BasicDBObject(LUGGAGE, asList(
                new BasicDBObject(WEIGHT, 1).append(COLOR, "black"),
                new BasicDBObject(WEIGHT, 2).append(COLOR, "red")
        ));
        given(collection.findOne(any(), any())).willReturn(model);

        CarReader carReader = carsRepository.find(c -> c.withId(PROPER_ID)).first();

        // when
        List<Luggage> luggage1 = carReader.getLuggage();
        List<Luggage> luggage2 = carReader.getLuggage();

        // then
        assertThat(luggage2).isSameAs(luggage1);
    }

}
