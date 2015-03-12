package com.futureprocessing.documentjuggler;

import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TransformersTest {

    public static final String ID = new ObjectId().toHexString();
    private CarsRepository carsRepository;

    @Mock
    private DB db;
    @Mock
    private DBCollection collection;
    @Mock
    private WriteResult writeResult;

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
    public void shouldModifyDocumentBeforeInsert() {
        //given
        final String brand = "BMW";
        final String otherKey = "otherKey";
        final String otherValue = "otherValue";

        //when
        carsRepository.preInsert(dbObject ->
                        dbObject.append(otherKey, otherValue)
        );
        carsRepository.insert(car -> car.withBrand(brand));

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.BRAND, brand).append(otherKey, otherValue);
        assertThat(insertedDocument).isEqualTo(expectedInsert);
    }

}
