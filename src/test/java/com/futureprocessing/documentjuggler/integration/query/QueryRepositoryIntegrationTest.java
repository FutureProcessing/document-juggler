package com.futureprocessing.documentjuggler.integration.query;

import com.futureprocessing.documentjuggler.QueryRepository;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.integration.BaseIntegrationTest;
import com.futureprocessing.documentjuggler.model.DefaultModel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryRepositoryIntegrationTest extends BaseIntegrationTest {

    private interface Movie extends DefaultModel<Movie> {
        @DbField("title")
        String getTitle();

        @DbField("title")
        void setTitle(String title);
    }

    private interface MovieQuery extends DefaultModel<MovieQuery> {
        @DbField("title")
        MovieQuery withTitle(String title);

        @DbField("director")
        MovieQuery withDirector(String director);
    }

    @Test
    public void shouldFindObjectWithDifferentQueryModel(){
        //given
        QueryRepository<Movie, MovieQuery> repository = new QueryRepository<Movie, MovieQuery>(db().getCollection("movies"),
                Movie.class, MovieQuery.class);
        repository.insert(movie -> movie.setTitle("Seven"));
        final String movieId = repository.insert(movie -> movie.setTitle("Fight club"));

        //when
        Movie movie = repository.find(movieQuery -> movieQuery.withTitle("Fight club")).first();

        //then
        assertThat(movie.getId()).isEqualTo(movieId);
        assertThat(movie.getTitle()).isEqualTo("Fight club");
    }

}
