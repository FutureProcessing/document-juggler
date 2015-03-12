package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransformersIntegrationTest extends BaseIntegrationTest {

    private final String brand = "BMW";
    private final String ADDITIONAL_FIELD = "ADDITIONAL_FIELD";
    private final String ADDITIONAL_VALUE = "ADDITIONAL_VALUE";


    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
        collection = db().getCollection(CarsDBModel.Car.COLLECTION);
    }

    @Test
    public void preInsertShouldModifyDocumentBeforeInsert() {
        //given

        //when
        repo.preInsert(dbObject ->
                        dbObject.append(ADDITIONAL_FIELD, ADDITIONAL_VALUE)
        );
        repo.insert(car -> car.withBrand(brand));

        //then
        BasicDBObject one = (BasicDBObject) collection.findOne();

        assertThat(one.getString(CarsDBModel.Car.BRAND)).isEqualTo(brand);
        assertThat(one.getString(ADDITIONAL_FIELD)).isEqualTo(ADDITIONAL_VALUE);
    }

    @Test
    public void preUpdateShouldModifyDocumentBeforeUpdate() {
        //given
        String id = repo.insert(car -> car.withBrand("Ford"));

        //when
        repo.preUpdate(dbObject -> {
                    BasicDBObject setObject = (BasicDBObject) dbObject.get("$set");
                    setObject.append(ADDITIONAL_FIELD, ADDITIONAL_VALUE);
                    return setObject;
                }
        );
        repo.find(car -> car.withId(id)).update(car -> car.withBrand(brand));

        //then
        BasicDBObject one = (BasicDBObject) collection.findOne();

        assertThat(one.getString(CarsDBModel.Car.BRAND)).isEqualTo(brand);
        assertThat(one.getString(ADDITIONAL_FIELD)).isEqualTo(ADDITIONAL_VALUE);
    }

}
