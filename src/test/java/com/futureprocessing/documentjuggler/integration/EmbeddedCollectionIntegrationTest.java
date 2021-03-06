package com.futureprocessing.documentjuggler.integration;


import com.futureprocessing.documentjuggler.example.cars.CarsRepository;
import com.futureprocessing.documentjuggler.example.cars.model.Luggage;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

public class EmbeddedCollectionIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(db());
    }

    @Test
    public void shouldInsertSingleEmbeddedDocument() {
        // given
        String color = "black";
        int weight = 1;

        // when
        String id = repo.insert(car -> car.withLuggage(
                luggage -> luggage
                        .withColor(color)
                        .withWeight(weight)
        ));

        // then
        List<Luggage> luggage = repo.find(car -> car.withId(id)).first().getLuggage();

        assertThat(luggage).hasSize(1);
        assertThat(luggage.get(0).getColor()).isEqualTo(color);
        assertThat(luggage.get(0).getWeight()).isEqualTo(weight);
    }

    @Test
    public void shouldInsertTwoEmbeddedDocument() {
        // given
        String color1 = "black";
        int weight1 = 1;

        String color2 = "red";
        int weight2 = 2;

        // when
        String id = repo.insert(car -> car.withLuggage(
                luggage1 -> luggage1
                        .withColor(color1)
                        .withWeight(weight1),
                luggage2 -> luggage2
                        .withColor(color2)
                        .withWeight(weight2)
        ));

        // then
        List<Luggage> luggage = repo.find(car -> car.withId(id)).first().getLuggage();

        assertThat(luggage).hasSize(2);
        assertThat(extractProperty("color").from(luggage)).containsExactly(color1, color2);
        assertThat(extractProperty("weight").from(luggage)).containsExactly(weight1, weight2);
    }

    @Test
    public void shouldReadSetOfEmbeddedDocuments() {
        // given
        String color = "black";
        int weight = 1;

        String id = repo.insert(car -> car.withRoofLuggage(
                luggage -> luggage
                        .withColor(color)
                        .withWeight(weight)
        ));

        // when
        Set<Luggage> set = repo.find(car -> car.withId(id)).first().getRoofLuggage();

        // then
        assertThat(set).hasSize(1);
        set.forEach(luggage -> {
            assertThat(luggage.getColor()).isEqualTo(color);
            assertThat(luggage.getWeight()).isEqualTo(weight);
        });
    }
}
