package com.futureprocessing.documentjuggler.integration;


import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IncIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
    }

    @Test
    public void shouldIncreaseExistingNumber() {
        // given
        int number = 21;
        int diff = 9;
        String id = repo.insert(car -> car.withSideNumber(number));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.withSideNumber(n -> n.increment(diff)));

        // then
        int sideNumber = repo.find(car -> car.withId(id)).first().getSideNumber();
        assertThat(sideNumber).isEqualTo(number + diff);
    }

    @Test
    public void shouldDecreaseExistingNumber() {
        // given
        int number = 21;
        int diff = -11;
        String id = repo.insert(car -> car.withSideNumber(number));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.withSideNumber(n-> n.increment(diff)));

        // then
        int sideNumber = repo.find(car -> car.withId(id)).first().getSideNumber();
        assertThat(sideNumber).isEqualTo(number + diff);
    }

    @Test
    public void shouldIncreaseNotExistingNumber() {
        // given
        int diff = 9;
        String id = repo.insert(car -> car.withModel("F1"));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.withSideNumber(n -> n.increment(diff)));

        // then
        int sideNumber = repo.find(car -> car.withId(id)).first().getSideNumber();
        assertThat(sideNumber).isEqualTo(diff);
    }
}
