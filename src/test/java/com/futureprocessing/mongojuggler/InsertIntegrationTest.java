package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InsertIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(new SimpleDBProvider(client(), DB_NAME));
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
}
