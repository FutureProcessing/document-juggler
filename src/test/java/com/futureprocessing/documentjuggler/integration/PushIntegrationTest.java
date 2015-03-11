package com.futureprocessing.documentjuggler.integration;


import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PushIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
    }

    @Test
    public void shouldPushToExistingList() {
        // given
        String id = repo.insert(car -> car.withOwners(asList("One")));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.addOwner("Two"));

        // then
        List<String> owners = repo.find(car -> car.withId(id)).first().getOwners();
        assertThat(owners).containsExactly("One", "Two");
    }

    @Test
    public void shouldPushTwoElementsToExistingList() {
        // given
        String id = repo.insert(car -> car.withOwners(asList("One")));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car
                                .addOwner("Two")
                                .addOwner("Three")
                );

        // then
        List<String> owners = repo.find(car -> car.withId(id)).first().getOwners();
        assertThat(owners).containsExactly("One", "Two", "Three");
    }

    @Test
    public void shouldCreateListIfNotExists() {
        // given
        String id = repo.insert(car -> car.withModel("Model"));

        // when
        repo.find(car -> car.withId(id))
                .update(car -> car.addOwner("One"));

        // then
        List<String> owners = repo.find(car -> car.withId(id)).first().getOwners();
        assertThat(owners).containsExactly("One");
    }
}
