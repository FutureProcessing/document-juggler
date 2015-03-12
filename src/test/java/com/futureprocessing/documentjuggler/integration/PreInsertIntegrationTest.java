package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PreInsertIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
        collection = db().getCollection(CarsDBModel.Car.COLLECTION);
    }

    @Test
    public void shouldModifyDocumentBeforeInsert() {
        //given
        final String brand = "BMW";
        final String additionalKey = "additionalKey";
        final String additionalValue = "additionalValue";

        //when
        repo.preInsert(dbObject ->
                        dbObject.append(additionalKey, additionalValue)
        );
        repo.insert(car -> car.withBrand(brand));

        //then
        BasicDBObject one = (BasicDBObject) collection.findOne();

        assertThat(one.getString(CarsDBModel.Car.BRAND)).isEqualTo(brand);
        assertThat(one.getString(additionalKey)).isEqualTo(additionalValue);
    }

}
