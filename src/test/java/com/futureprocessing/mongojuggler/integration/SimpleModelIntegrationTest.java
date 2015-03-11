package com.futureprocessing.mongojuggler.integration;

import com.futureprocessing.mongojuggler.example.cars.CarsRepository;
import com.futureprocessing.mongojuggler.example.movies.MoviesRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleModelIntegrationTest extends BaseIntegrationTest {

    private static MoviesRepository moviesRepository;

    @BeforeClass
    public static void init() throws Exception {
        moviesRepository = new MoviesRepository(db());
    }

    @Test
    public void shouldCreateMoviesRepository(){
        //given

        //when

        //then
        assertThat(moviesRepository).isNotNull();
    }


}
