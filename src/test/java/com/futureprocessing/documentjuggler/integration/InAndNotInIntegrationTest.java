package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Car;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class InAndNotInIntegrationTest extends BaseIntegrationTest {

    private static final String OWNER1 = "Kowalsky";
    private static final String OWNER2 = "O'Connel";
    private static final String OWNER3 = "Smith";
    private static CarsRepository repo;
    private static List owners;
    private static List<String> ownersCar1;
    private static List<String> ownersCar3;

    @BeforeClass
    public static void init() {
        repo = new CarsRepository(db());

        owners = new ArrayList<>();
        ownersCar1 = new ArrayList<>();
        ownersCar3 = new ArrayList<>();

        ownersCar1.add(OWNER1);
        ownersCar1.add(OWNER2);

        ownersCar3.add(OWNER3);
        ownersCar3.add(OWNER2);

        owners.add(ownersCar1);
        owners.add(ownersCar3);
    }

    @Before
    public void given() {
        repo.insert(car -> car.withSideNumber(1).withOwners(OWNER1, OWNER2));
        repo.insert(car -> car.withSideNumber(2).withOwners(OWNER2));
        repo.insert(car -> car.withSideNumber(3).withOwners(OWNER3, OWNER2));
    }

    @Test
    public void shouldFindInArray() {
        // given
        int[] sideNumbers = new int[]{2, 3};

        // when
        List<Car> cars = repo.find(car -> car.withSideNumberIn(sideNumbers)).all();

        // then
        assertThat(extractProperty("sideNumber").from(cars)).containsOnly(2, 3);
    }

    @Test
    public void shouldFindInList() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withOwnersIn(owners)).all();

        // then
        assertThat(extractProperty("owners").from(cars)).contains(ownersCar1, ownersCar3);
    }

    @Test
    public void shouldFindIfNotInArray() {
        // given
        int[] sideNumbers = new int[]{2, 3};

        // when
        List<Car> cars = repo.find(car -> car.withSideNumberNotIn(sideNumbers)).all();

        // then
        assertThat(extractProperty("sideNumber").from(cars)).containsOnly(1);
    }

    @Test
    public void shouldFindIfNotInList() {
        // given

        // when
        List<Car> cars = repo.find(car -> car.withOwnersNotIn(owners)).all();

        // then
        assertThat(extractProperty("owners").from(cars)).doesNotContain(ownersCar1, ownersCar3);
        assertThat(cars.size()).isEqualTo(1);
    }
}
