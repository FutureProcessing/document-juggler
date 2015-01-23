package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.example.CarsDBModel;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.Car;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class ReadIntegrationTest {

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
    public void shouldReadDocument() {
        // given
        String id = "h1";
        String brand = "Honda";
        String model = "HR-V";

        repo.insert(car -> car
                .withId(id)
                .withBrand(brand)
                .withModel(model));

        // when
        Car document = repo.find(car -> car.withId(id)).one();

        assertThat(document.getBrand()).isEqualTo(brand);
        assertThat(document.getModel()).isEqualTo(model);
    }

    @Test
    public void shouldReadList() {
        // given
        String brand = "Honda";
        String model1 = "HR-V";
        String model2 = "CR-V";

        repo.insert(car -> car.withBrand(brand).withModel(model1));
        repo.insert(car -> car.withBrand(brand).withModel(model2));

        // when
        List<Car> list = repo.find(car -> car.withBrand(brand)).all();

        // then
        assertThat(extractProperty("model").from(list)).containsExactly(model1, model2);
    }
}
