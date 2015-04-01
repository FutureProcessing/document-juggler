package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.QueryRepository;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.model.DefaultModel;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QueryRepositoryTest {

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

    @Mock
    private DBCollection collection;

    @Test
    public void shouldQueryWithSeparateQuerier() {
        //given
        QueryRepository<Movie, MovieQuery> repository = new QueryRepository<>(collection, Movie.class, MovieQuery.class);

        //when
        repository.find(movieQuery -> movieQuery.withTitle("Seven")).first();

        //then
        DBObject expectedQuery = new BasicDBObject("title", "Seven");
        verify(collection).findOne(eq(expectedQuery), any());
    }

    @Test
    public void shouldQueryWithDifferentQueryModel() {
        //given
        QueryRepository<Movie, MovieQuery> repository = new QueryRepository<>(collection, Movie.class, MovieQuery.class);

        //when
        repository.find(movieQuery -> movieQuery.withTitle("Seven")
                .withDirector("Fincher")).first();

        //then
        DBObject expectedQuery = new BasicDBObject("title", "Seven").append("director", "Fincher");
        verify(collection).findOne(eq(expectedQuery), any());
    }


}
