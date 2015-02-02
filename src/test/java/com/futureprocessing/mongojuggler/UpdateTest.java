package com.futureprocessing.mongojuggler;

import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.exception.InvalidNumberOfDocumentsAffected;
import com.futureprocessing.mongojuggler.write.UpdateResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.OWNERS;
import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.PASSENGERS_NAMES;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UpdateTest {

    public static final String ID = new ObjectId().toHexString();
    private CarsRepository carsRepository;

    @Mock
    private DB db;
    @Mock
    private DBCollection collection;
    @Mock
    private MongoDBProvider dbProvider;
    @Mock
    private WriteResult writeResult;

    @Before
    public void before() {
        given(dbProvider.db()).willReturn(db);
        given(db.getCollection(any())).willReturn(collection);
        given(collection.update(any(), any())).willReturn(writeResult);
        given(writeResult.getN()).willReturn(1);

        carsRepository = new CarsRepository(dbProvider);
    }

    @Test
    public void shouldUpdateDocumentInCorrectCollection() {
        //given

        //when
        carsRepository.update(car -> car.withId(ID)).with(car -> car.addPassengerName("Kowalski"));

        //then
        verify(db).getCollection(eq(CarsDBModel.Car.COLLECTION));
    }

    @Test
    public void shouldUpdateCorrectDocumentById() {
        //given

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.withModel("Corsa"));

        //then
        BasicDBObject expectedQuery = new BasicDBObject(CarsDBModel.Car.ID, new ObjectId(ID));
        verify(collection).update(eq(expectedQuery), any());
    }

    @Test
    public void shouldReturnUpdateResult() {
        //given

        //when
        UpdateResult result = carsRepository.update(car -> car.withId(ID))
                .with(car -> car.withModel("Corsa"));

        //then
        assertThat(result.getAffectedCount()).isEqualTo(1);
    }

    @Test
    public void shouldThrowInvalidNumberOfDocumentsAffectedException() {
        //given
        given(writeResult.getN()).willReturn(0);
        final String newBrand = "BMW";

        //when
        try {
            carsRepository.update(car -> car.withId(ID))
                    .with(car -> car.withBrand(newBrand))
                    .ensureOneUpdated();
        } catch (InvalidNumberOfDocumentsAffected e) {
            //then
            assertThat(e.getAffected()).isEqualTo(0);
            assertThat(e.getExpected()).isEqualTo(1);
            return;
        }

        fail("Should throw exception");
    }

    @Test
    public void shouldUpdateDocumentWithOneValue() {
        //given
        final String newBrand = "BMW";

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.withBrand(newBrand));

        //then
        BasicDBObject expectedSet = new BasicDBObject(CarsDBModel.Car.BRAND, newBrand);
        BasicDBObject expectedUpdate = new BasicDBObject("$set", expectedSet);
        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldUpdateDocumentWithTwoValues() {
        //given
        final String newBrand = "Opel";
        final String newModel = "corsa";

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car
                                .withBrand(newBrand)
                                .withModel(newModel)
                );

        //then
        BasicDBObject expectedSet = new BasicDBObject(CarsDBModel.Car.BRAND, newBrand)
                .append(CarsDBModel.Car.MODEL, newModel);
        BasicDBObject expectedUpdate = new BasicDBObject("$set", expectedSet);
        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldUnsetValueWhenNullPassed() {
        //given

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.withBrand(null));

        //then
        BasicDBObject expectedSet = new BasicDBObject(CarsDBModel.Car.BRAND, null);
        BasicDBObject expectedUpdate = new BasicDBObject("$unset", expectedSet);
        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldSetAndUnsetFields() {
        //given
        final String newModel = "Corsa";

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car
                                .withBrand(null)
                                .withModel(newModel)
                );

        //then
        BasicDBObject expectedUpdate = new BasicDBObject("$unset", new BasicDBObject(CarsDBModel.Car.BRAND, null))
                .append("$set", new BasicDBObject(CarsDBModel.Car.MODEL, newModel));
        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldUnsetFieldWhenPassedFalseBoolean() {
        //given

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.withAutomaticGearbox(false));

        //then
        BasicDBObject expectedUpdate = new BasicDBObject("$unset",
                new BasicDBObject(CarsDBModel.Car.AUTOMATIC_GEARBOX, null));

        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldUpdateEmbeddedDocument() {
        //given
        final String newFuel = "diesel";

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.engine(
                        engine -> engine.withFuel(newFuel)
                ));

        //then
        String fieldToSet = CarsDBModel.Car.ENGINE + "." + CarsDBModel.Car.Engine.FUEL;
        BasicDBObject expectedUpdate = new BasicDBObject("$set", new BasicDBObject(fieldToSet, newFuel));

        verify(collection).update(any(), eq(expectedUpdate));
    }


    @Test
    public void shouldUnsetValueInEmbeddedDocument() {
        //given

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.engine(
                        engine -> engine.withFuel(null)
                ));

        //then
        String fieldToSet = CarsDBModel.Car.ENGINE + "." + CarsDBModel.Car.Engine.FUEL;
        BasicDBObject expectedUpdate = new BasicDBObject("$unset", new BasicDBObject(fieldToSet, null));

        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldUpdateDocumentWithSetOfStrings() {
        //given
        final Set<String> passengers = new HashSet<>(asList("Adam", "Mark", "John"));

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.withPassengerNames(passengers));

        //then
        BasicDBObject expectedSet = new BasicDBObject(PASSENGERS_NAMES, passengers);
        BasicDBObject expectedUpdate = new BasicDBObject("$set", expectedSet);
        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldAddStringToSet() {
        //given
        final String newPassenger = "Adam";

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.addPassengerName(newPassenger));

        //then
        BasicDBObject expectedUpdate = new BasicDBObject("$addToSet",
                new BasicDBObject(PASSENGERS_NAMES, newPassenger));
        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldUpdateDocumentWithListOfStrings() {
        //given
        final List<String> owners = asList("Adam", "Mark", "John");

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.withOwners(owners));

        //then
        BasicDBObject expectedSet = new BasicDBObject(OWNERS, owners);
        BasicDBObject expectedUpdate = new BasicDBObject("$set", expectedSet);
        verify(collection).update(any(), eq(expectedUpdate));
    }

    @Test
    public void shouldAddStringToList() {
        //given
        final String newOwner = "Adam";

        //when
        carsRepository.update(car -> car.withId(ID))
                .with(car -> car.addOwner(newOwner));

        //then
        BasicDBObject expectedUpdate = new BasicDBObject("$push",
                new BasicDBObject(OWNERS, newOwner));
        verify(collection).update(any(), eq(expectedUpdate));
    }
}
