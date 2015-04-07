package com.futureprocessing.documentjuggler.integration;


import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static com.futureprocessing.documentjuggler.helper.Sets.asSet;
import static org.assertj.core.api.Assertions.assertThat;

public class AddToSetIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
    }

    @Test
    public void shouldPushToExistingSet() {
        // given
        String id = repo.insert(car -> car.withPassengerNames(asSet("One")));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.withPassengerNames(p -> p.addToSet("Two")));

        // then
        Set<String> passengers = repo.find(car -> car.withId(id)).first().getPassengersNames();
        assertThat(passengers).containsExactly("One", "Two");
    }

    @Test
    public void shouldPushTwoElementsToExistingSet() {
        // given
        String id = repo.insert(car -> car.withPassengerNames(asSet("One")));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.withPassengerNames(p -> p.addToSet("Two", "Three")));

        // then
        Set<String> passengers = repo.find(car -> car.withId(id)).first().getPassengersNames();
        assertThat(passengers).containsExactly("One", "Two", "Three");
    }

    @Test
    public void shouldCreateSetIfNotExists() {
        // given
        String id = repo.insert(car -> car.withModel("Model"));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.withPassengerNames(p -> p.addToSet("One")));

        // then
        Set<String> passengers = repo.find(car -> car.withId(id)).first().getPassengersNames();
        assertThat(passengers).containsExactly("One");
    }
}
