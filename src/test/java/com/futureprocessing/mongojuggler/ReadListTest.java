package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.mongodb.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.BRAND;
import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.MODEL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReadListTest {

    private CarsRepository carsRepository;

    @Mock
    private DB db;
    @Mock
    private DBCollection collection;
    @Mock
    private DBCursor cursor;
    @Mock
    private MongoDBProvider dbProvider;


    @Before
    public void before() {
        given(dbProvider.db()).willReturn(db);
        given(db.getCollection(any())).willReturn(collection);
        given(collection.find(any(), any())).willReturn(cursor);

        carsRepository = new CarsRepository(dbProvider);
    }

    @Test
    public void shouldReadListOfDocuments() {
        // given
        String brand = "Ford";
        String model1 = "Mondeo";
        String model2 = "Focus";

        BasicDBObject car1 = new BasicDBObject(BRAND, brand).append(MODEL, model1);
        BasicDBObject car2 = new BasicDBObject(BRAND, brand).append(MODEL, model2);

        given(cursor.hasNext()).willReturn(true, true, false);
        given(cursor.next()).willReturn(car1, car2);

        // when
        List<Car> cars = carsRepository.find(car -> car.withBrand(brand)).all();

        // then
        assertThat(extractProperty("model").from(cars)).containsExactly(model1, model2);
    }

    @Test
    public void shouldReturnEmptyListIfNoMatchingDocuments() {
        // given
        given(cursor.hasNext()).willReturn(false);

        // when
        List<Car> cars = carsRepository.find(car -> car.withBrand("Fiat")).all();

        // then
        assertThat(cars).isEmpty();
    }

    @Test
    public void shouldNotPassQueryWhenTryingToFindAll() {
        // given

        // when
        carsRepository.find().all(MODEL);

        // then
        verify(collection).find(isNull(DBObject.class), any(DBObject.class));
    }

    @Test
    public void shouldPassSkipAndLimitWhenTryingToGetOnePageOfResults() {
        // given

        // when
        carsRepository.find().skip(1).limit(10).all();

        // then
        verify(cursor).limit(eq(10));
        verify(cursor).skip(eq(1));
    }

    @Test
    public void shouldThrowFieldNotLoadedExceptionWhenAccessingNotLoadedField() {
        // given
        BasicDBObject dbObject = new BasicDBObject();
        given(cursor.hasNext()).willReturn(true, false);
        given(cursor.next()).willReturn(dbObject);

        List<Car> cars = carsRepository.find(car -> car.withBrand("fiat")).all(MODEL);

        // when
        try {
            cars.get(0).getBrand();
        } catch (FieldNotLoadedException e) {
            // then
            return;
        }

        fail("Should thrown exception");
    }
}
