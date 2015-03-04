package com.futureprocessing.mongojuggler.integration;


import com.futureprocessing.mongojuggler.SimpleDBProvider;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
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
                .update(car -> car.increaseSideNumber(diff));

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
                .update(car -> car.increaseSideNumber(diff));

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
                .update(car -> car.increaseSideNumber(diff));

        // then
        int sideNumber = repo.find(car -> car.withId(id)).first().getSideNumber();
        assertThat(sideNumber).isEqualTo(diff);
    }
}
