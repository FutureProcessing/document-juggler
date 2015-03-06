package com.futureprocessing.mongojuggler.integration;

import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.futureprocessing.mongojuggler.exception.InvalidNumberOfDocumentsAffected;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.futureprocessing.mongojuggler.example.CarsDBModel.Car.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RemoveIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
        collection = client().getDB(DB_NAME).getCollection(CarsDBModel.Car.COLLECTION);
    }

    @Test
    public void shouldRemoveDocument() {
        //given
        String brand = "Honda";
        String model = "HR-V";

        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel(model));

        //when
        repo.find(car -> car.withId(id)).remove().ensureOneDeleted();

        //then
        Car.Reader car = repo.find(c -> c.withId(id)).first();
        assertThat(car).isNull();

        BasicDBObject document = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(id)));
        assertThat(document).isNull();
    }

    @Test
    public void shouldThrowExceptionIfDocumentNotFound() {
        //given
        String brand = "Honda";
        String model = "HR-V";

        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel(model));

        //when

        try {
            repo.find(car -> car.withId(new ObjectId().toHexString())).remove().ensureOneDeleted();
        } catch (InvalidNumberOfDocumentsAffected e) {
            //then
            assertThat(e.getAffected()).isEqualTo(0);
            assertThat(e.getExpected()).isEqualTo(1);
            return;
        }

        fail("Should have thrown exception");
    }
}
