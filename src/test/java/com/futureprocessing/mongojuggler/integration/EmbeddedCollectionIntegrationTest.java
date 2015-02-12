package com.futureprocessing.mongojuggler.integration;


import com.futureprocessing.mongojuggler.SimpleDBProvider;
import com.futureprocessing.mongojuggler.example.CarsRepository;
import com.futureprocessing.mongojuggler.example.model.Luggage;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedCollectionIntegrationTest extends BaseIntegrationTest {

    private static CarsRepository repo;

    @BeforeClass
    public static void init() throws Exception {
        repo = new CarsRepository(new SimpleDBProvider(client(), DB_NAME));
    }

    @Test
    public void shouldInsertSingleEmbeddedDocument() {
        // given
        String color = "black";
        int weight = 1;

        // when
        String id = repo.insert(car -> car.luggage(
                luggage -> luggage
                        .withColor(color)
                        .withWeight(weight)
        ));

        // then
        List<Luggage.Read> luggage = repo.find(car -> car.withId(id)).first().getLuggage();

        assertThat(luggage).hasSize(1);
        assertThat(luggage.get(0).getColor()).isEqualTo(color);
        assertThat(luggage.get(0).getWeight()).isEqualTo(weight);
    }
}
