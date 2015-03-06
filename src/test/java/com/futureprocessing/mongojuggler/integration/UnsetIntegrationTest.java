package com.futureprocessing.mongojuggler.integration;

import com.futureprocessing.mongojuggler.example.cars.CarsDBModel;
import com.futureprocessing.mongojuggler.example.cars.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.futureprocessing.mongojuggler.example.cars.CarsDBModel.Car.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UnsetIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
        collection = client().getDB(DB_NAME).getCollection(CarsDBModel.Car.COLLECTION);
    }

    @Test
    public void shouldUnsetField() {
        //given
        String brand = "Honda";
        String model = "HR-V";

        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel(model));

        //when
        repo.find(car -> car.withId(id))
                .update(car -> car.withoutModel())
                .ensureOneUpdated();

        //then
        BasicDBObject document = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(id)));

        assertThat(document.get(BRAND)).isEqualTo(brand);
        assertThat(document.containsField(MODEL)).isFalse();
    }

    @Test
    public void shouldUnsetEmbeddedDocumentField() {
        //given

        String id = repo.insert(car -> car
                .engine(engine -> engine.withFuel("Diesel")
                        .withCylindersNumber(6)));

        //when
        repo.find(car -> car.withId(id))
                .update(car -> car.engine(engine -> engine.withoutCylindersNumber()))
                .ensureOneUpdated();

        //then
        BasicDBObject document = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(id)));

        BasicDBObject engine = (BasicDBObject) document.get(ENGINE);
        assertThat(engine.containsField(Engine.CYLINDERS_NUMBER)).isFalse();
    }


}
