package com.futureprocessing.mongojuggler.integration;

import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
        collection = client().getDB(DB_NAME).getCollection(CarsDBModel.Car.COLLECTION);
    }

    @Test
    public void shouldSetNullValue() {
        //given
        String brand = "Honda";
        String model = "HR-V";

        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel(model));

        //when
        repo.find(car -> car.withId(id))
                .update(car -> car.withModel(null))
                .ensureOneUpdated();

        //then
        BasicDBObject document = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(id)));

        assertThat(document.get(BRAND)).isEqualTo(brand);
        assertThat(document.containsField(MODEL)).isTrue();
        assertThat(document.get(MODEL)).isNull();
    }

    @Test
    public void shouldSetCorrectValue() {
        //given
        String brand = "Honda";
        String model = "HR-V";

        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel(null));

        //when
        repo.find(car -> car.withId(id))
                .update(car -> car.withModel(model))
                .ensureOneUpdated();

        //then
        BasicDBObject document = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(id)));

        assertThat(document.get(BRAND)).isEqualTo(brand);
        assertThat(document.containsField(MODEL)).isTrue();
        assertThat(document.get(MODEL)).isEqualTo(model);
    }
}
