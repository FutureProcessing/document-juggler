package com.futureprocessing.mongojuggler;

import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.exception.UnsupportedActionException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Fail.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InsertTest {

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
    public void shouldInsertNewDocumentIntoCorrectCollection() {
        //given

        //when
        carsRepository.insert(car -> car.withBrand("brand"));

        //then
        verify(db).getCollection(Matchers.eq(CarsDBModel.Car.COLLECTION));
    }

    @Test
    public void shouldInsertNewDocument() {
        //given
        final String brand = "BMW";

        //when
        carsRepository.insert(car -> car.withBrand(brand));

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.BRAND, brand);
        verify(collection).insert(eq(expectedInsert));
    }

    @Test
    public void shouldInsertNewDocumentWithDate() {
        //given
        final String newBrand = "Fiat";
        final Date newReleaseDate = new Date();

        //when
        carsRepository.insert(car -> car
                        .withBrand(newBrand)
                        .withReleaseDate(newReleaseDate)
        );

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.BRAND, newBrand).append(CarsDBModel.Car.RELEASE_DATE, newReleaseDate);
        verify(collection).insert(eq(expectedInsert));
    }

    @Test
    public void shouldInsertNewDocumentWithInternalDocument() {
        //given
        final String value = "Opel";
        final String fuel = "gas";
        final int cylinders = 4;
        final Date date = new Date();

        //when
        carsRepository.insert(car -> car
                        .withBrand(value)
                        .engine(engine -> engine
                                        .withFuel(fuel)
                                        .withCylindersNumber(cylinders)
                        )
                        .withReleaseDate(date)
        );

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.BRAND, value)
                .append(CarsDBModel.Car.RELEASE_DATE, date)
                .append(CarsDBModel.Car.ENGINE, new BasicDBObject(CarsDBModel.Car.Engine.FUEL, fuel)
                        .append(CarsDBModel.Car.Engine.CYLINDERS_NUMBER, cylinders));
        verify(collection).insert(eq(expectedInsert));
    }

    @Test
    public void shouldInsertNewDocumentWithListOfStrings() {
        //given
        final List<String> passengers = asList("Adam", "Mark", "John");

        //when
        carsRepository.insert(car -> car.withPassengerNames(passengers));

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.PASSENGERS_NAMES, passengers);
        verify(collection).insert(eq(expectedInsert));
    }

    @Test
    public void shouldThrowUnsupportedActionExceptionWhenAddingToListWhileInsert() {
        //given
        final String newPassenger = "Adam";

        try {
            //when
            carsRepository.insert(car -> car.addPassengerName(newPassenger));
        } catch (UnsupportedActionException e) {
            return;
        }

        //then
        fail("Should throw exception");
    }

}
