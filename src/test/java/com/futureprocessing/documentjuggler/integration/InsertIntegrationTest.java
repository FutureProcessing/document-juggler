package com.futureprocessing.documentjuggler.integration;


import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.futureprocessing.documentjuggler.example.cars.CarsDBModel.Car.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InsertIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
        collection = client().getDB(DB_NAME).getCollection(CarsDBModel.Car.COLLECTION);
    }

    @Test
    public void shouldInsertDocument() {
        // given
        String brand = "Honda";
        String model = "HR-V";

        // when
        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel(model));

        // then
        DBObject document = collection.findOne(new BasicDBObject(ID, new ObjectId(id)));

        assertThat(document.get(BRAND)).isEqualTo(brand);
        assertThat(document.get(MODEL)).isEqualTo(model);
    }

    @Test
    public void shouldReturnInsertedDocumentId() {
        // given

        // when
        String id = repo.insert(car -> car.withBrand("Ford"));

        // then
        assertThat(id).isNotEmpty();
    }

    @Test
    public void shouldSetNullValue() {
        //given
        String brand = "Honda";

        //when
        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel((String)null));

        //then
        BasicDBObject document = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(id)));

        assertThat(document.get(BRAND)).isEqualTo(brand);
        assertThat(document.containsField(MODEL)).isTrue();
        assertThat(document.get(MODEL)).isNull();
    }
}
