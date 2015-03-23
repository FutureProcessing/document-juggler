package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class GreaterLessIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() {
        repo = new CarsRepository(db());
    }

    @Before
    public void given() {
        repo.insert(car -> car.withSideNumber(1));
        repo.insert(car -> car.withSideNumber(2));
        repo.insert(car -> car.withSideNumber(3));
    }

    @Test
    public void shouldFindGraterThan() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withSideNumberGreaterThan(1)).all();

        // then
        assertThat(extractProperty("sideNumber").from(cars)).containsOnly(2, 3);
    }

    @Test
    public void shouldFindLessThan() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withSideNumberLessThan(3)).all();

        // then
        assertThat(extractProperty("sideNumber").from(cars)).containsOnly(1, 2);
    }

    @Test
    public void shouldFindGraterThanEqual() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withSideNumberGreaterThanEqual(2)).all();

        // then
        assertThat(extractProperty("sideNumber").from(cars)).containsOnly(2, 3);
    }

    @Test
    public void shouldFindLessThanEqual() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withSideNumberLessThanEqual(2)).all();

        // then
        assertThat(extractProperty("sideNumber").from(cars)).containsOnly(1, 2);
    }
}
