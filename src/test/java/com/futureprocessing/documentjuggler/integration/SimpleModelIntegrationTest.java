package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.example.movies.MoviesRepository;
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
