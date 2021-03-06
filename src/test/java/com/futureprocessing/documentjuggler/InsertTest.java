package com.futureprocessing.documentjuggler;

import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InsertTest {

    private CarsRepository carsRepository;

    @Mock
    private DB db;
    @Mock
    private DBCollection collection;

    private ObjectId insertedId = new ObjectId();
    private BasicDBObject insertedDocument;


    @Before
    public void before() {
        given(db.getCollection(any())).willReturn(collection);

        given(collection.insert(Mockito.<DBObject[]>any())).will(invocationOnMock -> {
            BasicDBObject doc = (BasicDBObject) invocationOnMock.getArguments()[0];
            insertedDocument = (BasicDBObject) doc.copy();
            doc.append("_id", insertedId);
            return null;
        });

        carsRepository = new CarsRepository(db);
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
        assertThat(insertedDocument).isEqualTo(expectedInsert);
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
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.BRAND, newBrand)
                .append(CarsDBModel.Car.RELEASE_DATE, newReleaseDate);
        assertThat(insertedDocument).isEqualTo(expectedInsert);
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
        assertThat(insertedDocument).isEqualTo(expectedInsert);
    }

    @Test
    public void shouldInsertNewDocumentWithSetOfStrings() {
        //given
        final Set<String> passengers = new HashSet<>(asList("Adam", "Mark", "John"));

        //when
        carsRepository.insert(car -> car.withPassengerNames(passengers));

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.PASSENGERS_NAMES, passengers);
        assertThat(insertedDocument).isEqualTo(expectedInsert);
    }

    @Test
    public void shouldInsertNewDocumentWithListOfStrings() {
        //given
        final List<String> owners = asList("Adam", "Mark", "John");

        //when
        carsRepository.insert(car -> car.withOwners(owners));

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.OWNERS, owners);
        assertThat(insertedDocument).isEqualTo(expectedInsert);
    }

    //todo enable when there will be support for mixed reader, inserter, updater interfaces
    /*@Test
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
*/
    @Test
    public void shouldReturnInsertedDocumentId() {
        // given

        // when
        String id = carsRepository.insert(car -> car.withBrand("Honda"));

        // then
        assertThat(id).isEqualTo(insertedId.toHexString());
    }

}
