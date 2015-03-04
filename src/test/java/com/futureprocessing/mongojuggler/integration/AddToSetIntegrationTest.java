package com.futureprocessing.mongojuggler.integration;


import com.futureprocessing.mongojuggler.SimpleDBProvider;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.helper.Sets;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.futureprocessing.mongojuggler.helper.Sets.asSet;
import static java.util.Arrays.asList;
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
                .update(car -> car.addPassengerName("Two"));

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
                .update(car -> car
                                .addPassengerName("Two")
                                .addPassengerName("Three")
                );

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
                .update(car -> car.addPassengerName("One"));

        // then
        Set<String> passengers = repo.find(car -> car.withId(id)).first().getPassengersNames();
        assertThat(passengers).containsExactly("One");
    }
}
