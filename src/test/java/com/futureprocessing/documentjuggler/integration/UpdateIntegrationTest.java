package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsDBModel;
import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.bson.types.ObjectId;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.futureprocessing.documentjuggler.example.cars.CarsDBModel.Car.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;
    private static DBCollection collection;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
        collection = db().getCollection(CarsDBModel.Car.COLLECTION);
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
        String modelHRV = "HR-V";

        String modelAccord = "Accord";

        String idHRV = repo.insert(car -> car
                .withBrand(brand)
                .withModel(modelHRV));

        String idAccord = repo.insert(car -> car
                .withBrand(brand)
                .withModel(modelAccord));

        //when
        repo.find(car -> car.withBrand(brand))
                .update(car -> car.withBrand(brand))
                .ensureUpdated(2);

        //then
        BasicDBObject documentHRV = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(idHRV)));
        BasicDBObject documentAccord = (BasicDBObject) collection.findOne(new BasicDBObject(ID, new ObjectId(idAccord)));

        assertThat(documentHRV.get(BRAND)).isEqualTo(brand);
        assertThat(documentHRV.containsField(MODEL)).isTrue();
        assertThat(documentHRV.get(MODEL)).isEqualTo(modelHRV);

        assertThat(documentAccord.get(BRAND)).isEqualTo(brand);
        assertThat(documentAccord.containsField(MODEL)).isTrue();
        assertThat(documentAccord.get(MODEL)).isEqualTo(modelAccord);
    }
}
