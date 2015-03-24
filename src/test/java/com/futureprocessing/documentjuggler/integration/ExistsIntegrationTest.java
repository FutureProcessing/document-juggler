package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistsIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    private static final String OWNER1 = "Kowalsky";
    private static final String OWNER2 = "O'Connel";
    private static final String OWNER3 = "Smith";

    @BeforeClass
    public static void init() {
        repo = new CarsRepository(db());
    }

    @Before
    public void given() {
        repo.insert(car -> car.withSideNumber(1).withOwners(OWNER1, OWNER2));
        repo.insert(car -> car.withSideNumber(2).withOwners(OWNER3));
        repo.insert(car -> car.withSideNumber(3));
    }

    @Test
    public void shouldFindIfOwnerExists() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withOwnersExists(true)).all();


        // then
        assertThat(cars.size()).isEqualTo(2);
        assertThat(cars.get(0).getOwners()).isNotNull();
        assertThat(cars.get(1).getOwners()).isNotNull();
    }

    @Test
    public void shouldFindIfOwnerNotExists() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withOwnersExists(false)).all();

        // then
        assertThat(cars.size()).isEqualTo(1);
        assertThat(cars.get(0).getOwners()).isNull();
    }
}
