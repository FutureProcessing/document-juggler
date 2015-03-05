package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.futureprocessing.mongojuggler.exception.LimitAlreadyPresentException;
import com.futureprocessing.mongojuggler.exception.SkipAlreadyPresentException;
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
import static org.mockito.Matchers.*;
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

        carsRepository = new CarsRepository(dbProvider.db());
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
        List<Car.Reader> carReaders = carsRepository.find(car -> car.withBrand(brand)).all();

        // then
        assertThat(extractProperty("model").from(carReaders)).containsExactly(model1, model2);
    }

    @Test
    public void shouldReturnEmptyListIfNoMatchingDocuments() {
        // given
        given(cursor.hasNext()).willReturn(false);

        // when
        List<Car.Reader> carReaders = carsRepository.find(car -> car.withBrand("Fiat")).all();

        // then
        assertThat(carReaders).isEmpty();
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
    public void shouldThrowSkipAlreadyPresentExceptionWhenSkipIsPassedTwice() {
        // given

        // when
        try {
            carsRepository.find().skip(1).skip(10).all();
        } catch (SkipAlreadyPresentException e) {
            //then
            return;
        }

        // then
        fail("Should throw SkipAlreadyPresentException exception");
    }

    @Test
    public void shouldThrowLimitAlreadyPresentExceptionWhenLimitIsPassedTwice() {
        // given

        // when
        try {
            carsRepository.find().limit(1).limit(10).all();
        } catch (LimitAlreadyPresentException e) {
            //then
            return;
        }

        // then
        fail("Should throw LimitAlreadyPresentException exception");
    }

    @Test
    public void shouldThrowFieldNotLoadedExceptionWhenAccessingNotLoadedField() {
        // given
        BasicDBObject dbObject = new BasicDBObject();
        given(cursor.hasNext()).willReturn(true, false);
        given(cursor.next()).willReturn(dbObject);

        List<Car.Reader> carReaders = carsRepository.find(car -> car.withBrand("fiat")).all(MODEL);

        // when
        try {
            carReaders.get(0).getBrand();
        } catch (FieldNotLoadedException e) {
            // then
            return;
        }

        fail("Should thrown exception");
    }
}
