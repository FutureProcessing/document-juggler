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
}
