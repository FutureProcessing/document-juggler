package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InsertIntegrationTest {

    private static MongoClient client;
    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        client = new MongoClient("localhost", 37017);
        repo = new CarsRepository(new SimpleDBProvider(client, "test"));
        collection = client.getDB("test").getCollection(CarsDBModel.Car.COLLECTION);
    }

    @After
    public void cleanDB() {
        client.dropDatabase("test");
    }

    @Test
    public void shouldInsertDocument() {
        // given
        String id = "h1";
        String brand = "Honda";
        String model = "HR-V";

        // when
        repo.insert(car -> car
                .withId(id)
                .withBrand(brand)
                .withModel(model));

        // then
        DBObject document = collection.findOne(new BasicDBObject(ID, id));

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
}
