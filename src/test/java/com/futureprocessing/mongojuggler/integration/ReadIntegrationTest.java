package com.futureprocessing.mongojuggler.integration;


import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.Car;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class ReadIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
    }

    @Test
    public void shouldReadDocument() {
        // given
        String brand = "Honda";
        String model = "HR-V";

        String id = repo.insert(car -> car
                .withBrand(brand)
                .withModel(model));

        // when
        Car.Reader document = repo.find(car -> car.withId(id)).first();

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
        List<Car.Reader> list = repo.find(car -> car.withBrand(brand)).all();

        // then
        assertThat(extractProperty("model").from(list)).containsExactly(model1, model2);
    }

    @Test
    public void shouldReadOnePageOfResults() {
        // given
        String brand = "Honda";
        String model1 = "HR-V";
        String model2 = "CR-V";
        String model3 = "Accord";

        repo.insert(car -> car.withBrand(brand).withModel(model1));
        repo.insert(car -> car.withBrand(brand).withModel(model2));
        repo.insert(car -> car.withBrand(brand).withModel(model3));

        // when
        List<Car.Reader> list = repo.find(car -> car.withBrand(brand)).skip(1).limit(1).all();

        // then
        assertThat(extractProperty("model").from(list)).containsExactly(model2);
    }

    @Test
    public void shouldReadAllCars() {
        // given
        String brand = "Honda";
        String model1 = "HR-V";
        String model2 = "CR-V";
        String model3 = "Accord";

        repo.insert(car -> car.withBrand(brand).withModel(model1));
        repo.insert(car -> car.withBrand(brand).withModel(model2));
        repo.insert(car -> car.withBrand(brand).withModel(model3));

        // when
        List<Car.Reader> list = repo.find().all();

        // then
        assertThat(extractProperty("model").from(list)).containsExactly(model1, model2, model3);
    }
}
