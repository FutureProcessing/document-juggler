package com.futureprocessing.mongojuggler.integration;


import com.futureprocessing.mongojuggler.SimpleDBProvider;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PushIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(new SimpleDBProvider(client(), DB_NAME));
    }

    @Test
    public void shouldPushToExistingList() {
        // given
        String id = repo.insert(car -> car.withOwners(asList("One")));

        // when
        repo.update(car -> car.withId(id))
                .with(car -> car.addOwner("Two"));

        // then
        List<String> owners = repo.find(car -> car.withId(id)).first().getOwners();
        assertThat(owners).containsExactly("One", "Two");
    }

    @Test
    public void shouldPushTwoElementsToExistingList() {
        // given
        String id = repo.insert(car -> car.withOwners(asList("One")));

        // when
        repo.update(car -> car.withId(id))
                .with(car -> car
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
        repo.update(car -> car.withId(id))
                .with(car -> car.addOwner("One"));

        // then
        List<String> owners = repo.find(car -> car.withId(id)).first().getOwners();
        assertThat(owners).containsExactly("One");
    }
}
