package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class QueryWithEmbeddedFieldsIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() {
        repo = new CarsRepository(db());
    }

    @Test
    public void shouldQueryWithEmbeddedFields() {
        // given
        String id1 = repo.insert(car -> car
                .withModel("Mustang")
                .engine(engine -> engine.withCylindersNumber(8)));

        String id2 = repo.insert(car -> car
                .withModel("Charger")
                .engine(engine -> engine.withCylindersNumber(8)));

        repo.insert(car -> car
                .withModel("F50")
                .engine(engine -> engine.withCylindersNumber(12)));

        // when
        List<Car> cars = repo.find(car -> car.engine(engine -> engine.withCylindersNumber(8))).all();

        // then
        assertThat(extractProperty("id").from(cars)).containsOnly(id1, id2);
    }
}
