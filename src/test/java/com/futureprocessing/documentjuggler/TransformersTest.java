package com.futureprocessing.documentjuggler;

import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransformersTest {

    private final String ADDITIONAL_OPERATION = "ADDITIONAL_OPERATION";
    private final String ADDITIONAL_FIELD = "ADDITIONAL_FIELD";
    private final String ADDITIONAL_VALUE = "ADDITIONAL_VALUE";

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

        //when
        carsRepository.preInsert(dbObject ->
                        dbObject.append(ADDITIONAL_FIELD, ADDITIONAL_VALUE)
        );
        carsRepository.insert(car -> car.withBrand(brand));

        //then
        DBObject expectedInsert = new BasicDBObject(CarsDBModel.Car.BRAND, brand).append(ADDITIONAL_FIELD, ADDITIONAL_VALUE);
        assertThat(insertedDocument).isEqualTo(expectedInsert);
    }

    @Test
    public void shouldModifyDocumentBeforeUpdate() {
        //given
        final String brand = "BMW";

        ArgumentCaptor<BasicDBObject> captor = ArgumentCaptor.forClass(BasicDBObject.class);
        when(collection.update(any(), captor.capture())).thenReturn(writeResult);


        //when
        carsRepository.preUpdate(dbObject ->
                        dbObject.append(ADDITIONAL_OPERATION, new BasicDBObject(ADDITIONAL_FIELD, ADDITIONAL_VALUE))
        );
        carsRepository.find().update(car -> car.withBrand(brand));

        //then
        DBObject expectedUpdate = new BasicDBObject("$set", new BasicDBObject(CarsDBModel.Car.BRAND, brand))
                .append(ADDITIONAL_OPERATION, new BasicDBObject(ADDITIONAL_FIELD, ADDITIONAL_VALUE));
        assertThat(captor.getValue()).isEqualTo(expectedUpdate);
    }


}
