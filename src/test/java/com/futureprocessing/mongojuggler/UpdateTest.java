package com.futureprocessing.mongojuggler;

import com.futureprocessing.mongojuggler.example.cars.CarsDBModel;
import com.futureprocessing.mongojuggler.example.cars.CarsRepository;
import com.futureprocessing.mongojuggler.exception.InvalidNumberOfDocumentsAffected;
import com.futureprocessing.mongojuggler.update.UpdateResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

        carsRepository = new CarsRepository(dbProvider.db());
    }

    @Test
    public void shouldUpdateDocumentInCorrectCollection() {
        //given

        //when
        carsRepository.find(car -> car.withId(ID)).update(car -> car.addPassengerName("Kowalski"));

        //then
        verify(db).getCollection(eq(CarsDBModel.Car.COLLECTION));
    }


    @Test
    public void shouldReturnUpdateResult() {
        //given

        //when
        UpdateResult result = carsRepository.find(car -> car.withId(ID))
                .update(car -> car.withModel("Corsa"));

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
            carsRepository.find(car -> car.withId(ID))
                    .update(car -> car.withBrand(newBrand))
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
