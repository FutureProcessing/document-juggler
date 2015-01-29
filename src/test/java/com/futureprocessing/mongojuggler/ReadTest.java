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
    public void shouldThrowFieldNotLoadedExceptionWhenAccessingNotLoadedField() {
        // given
        BasicDBObject dbObject = new BasicDBObject();
        given(collection.findOne(any(), any())).willReturn(dbObject);

        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(MODEL);

        // when
        try {
            entity.getBrand();
        } catch (FieldNotLoadedException e) {
            // then
            return;
        }

        fail("Should thrown exception");
    }

    @Test
    public void shouldContainValue() {
        // given
        final String brand = "BMW";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(BRAND);

        // then
        assertThat(entity.getBrand()).isEqualTo(brand);
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
    public void shouldFetchDataWithProjection() {
        // given
        BasicDBObject projection = new BasicDBObject(CarsDBModel.Car.BRAND, 1);

        final String brand = "Alfa Romeo";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand);
        given(collection.findOne(any(), eq(projection))).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(BRAND);

        // then
        assertThat(entity.getBrand()).isEqualTo(brand);
    }

    @Test
    public void shouldFetchAllFieldsWhenNoProjectionSpecified() {
        // given
        final String brand = "BMW";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first();

        // then
        assertThat(entity.getBrand()).isEqualTo(brand);
    }

    @Test
    public void shouldFetchEmbeddedDocument() {
        // given
        final String brand = "TestValue";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand)
                .append(ENGINE, new BasicDBObject(CarsDBModel.Car.Engine.FUEL, "diesel"));

        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first();

        // then
        assertThat(entity.getEngine()).isInstanceOf(Engine.class);
    }

    @Test
    public void embeddedDocumentShouldContainFields() {
        // given
        final String brand = "TestValue";
        final String fuel = "PB98";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand)
                .append(ENGINE, new BasicDBObject(CarsDBModel.Car.Engine.FUEL, fuel));

        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first();

        // then
        assertThat(entity.getEngine().getFuel()).isEqualTo(fuel);
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
    public void shouldQueryForObjectWithField() {
        // given
        given(collection.findOne(any(), any())).willReturn(new BasicDBObject());
        DBObject expectedQuery = new BasicDBObject(BRAND, "Fiat");

        // when
        carsRepository.find(car -> car.withBrand("Fiat")).first();

        // then
        verify(collection).findOne(eq(expectedQuery), any());

    }


    @Test
    public void shouldFetchBooleanValueTrue() {
        // given
        BasicDBObject dbObject = new BasicDBObject(AUTOMATIC_GEARBOX, true);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(AUTOMATIC_GEARBOX);

        // then
        assertThat(entity.isAutomaticGearbox()).isTrue();
    }

    @Test
    public void shouldFetchBooleanValueFalse() {
        // given
        BasicDBObject dbObject = new BasicDBObject(AUTOMATIC_GEARBOX, false);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(AUTOMATIC_GEARBOX);

        // then
        assertThat(entity.isAutomaticGearbox()).isFalse();
    }

    @Test
    public void booleanValueShouldBeFalseWhenNotPresent() {
        // given
        BasicDBObject dbObject = new BasicDBObject();
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(AUTOMATIC_GEARBOX);

        // then
        assertThat(entity.isAutomaticGearbox()).isFalse();
    }

    @Test
    public void shouldReadListOfStrings() {
        // given
        List<String> passengers = asList("Adam", "Max", "Kolonko");
        BasicDBObject dbObject = new BasicDBObject(PASSENGERS_NAMES, passengers);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car entity = carsRepository.find(car -> car.withId(PROPER_ID)).first(PASSENGERS_NAMES);

        // then
        assertThat(entity.getPassengersNames()).isEqualTo(passengers);
    }

    @Test
    public void shouldContainValueLambda() {
        // given
        final String brand = "BMW";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car car = carsRepository.find(carQuery -> carQuery.withId(PROPER_ID))
                .first(ID, BRAND);

        // then
        assertThat(car.getBrand()).isEqualTo(brand);
    }

    @Test
    public void shouldQueryForIdLambda() {
        // given
        final String brand = "BMW";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car car = carsRepository.find(carQuery -> carQuery.withId(PROPER_ID))
                .first(ID, BRAND);


        // then
        BasicDBObject expectedQuery = new BasicDBObject(ID, new ObjectId(PROPER_ID));
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldQueryWithCorrectProjectionLambda() {
        // given
        final String brand = "BMW";
        BasicDBObject dbObject = new BasicDBObject(BRAND, brand);
        given(collection.findOne(any(), any())).willReturn(dbObject);

        // when
        Car car = carsRepository.find(carQuery -> carQuery.withId(PROPER_ID))
                .first(ID, BRAND);

        // then
        BasicDBObject expectedProjection = new BasicDBObject(CarsDBModel.Car.BRAND, 1)
                .append(CarsDBModel.Car.ID, 1);

        verify(collection).findOne(any(), eq(expectedProjection));
    }

}
