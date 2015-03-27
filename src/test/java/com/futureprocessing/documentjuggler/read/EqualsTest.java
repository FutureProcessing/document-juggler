package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Equals;
import com.futureprocessing.documentjuggler.commons.EqualsProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static com.futureprocessing.documentjuggler.commons.EqualsProvider.BaseEquals.isEqualClass;
import static com.futureprocessing.documentjuggler.read.ReadProxy.create;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

public class EqualsTest {

    private Mapper<ReadCommand> mapper;

    @Before
    public void before() {
        mapper = ReadMapper.map(MovieWithDefaultEqualsBehaviour.class);
    }

    private interface MovieWithDefaultEqualsBehaviour {

        @DbField("title")
        String getTitle();

        @DbField("title")
        MovieWithDefaultEqualsBehaviour withTitle(String title);

        @DbField("director")
        String getDirector();

        @DbField("director")
        MovieWithDefaultEqualsBehaviour withDirector(String director);
    }

    @Test
    public void sameInstanceShouldBeEqual() {
        //given
        DBObject dbObject = new BasicDBObject();

        //when
        MovieWithDefaultEqualsBehaviour movie = create(MovieWithDefaultEqualsBehaviour.class, mapper, dbObject, emptySet());

        //then
        assertThat(movie.equals(movie)).isTrue();
        assertThat(movie).isEqualTo(movie);
    }

    @Test
    public void shouldBeEqualWithTheSameDbObject() {
        //given
        DBObject dbObject = new BasicDBObject("field", "value");

        //when
        MovieWithDefaultEqualsBehaviour movie1 = create(MovieWithDefaultEqualsBehaviour.class, mapper, dbObject, emptySet());
        MovieWithDefaultEqualsBehaviour movie2 = create(MovieWithDefaultEqualsBehaviour.class, mapper, dbObject, emptySet());

        //then
        assertThat(movie1.equals(movie2)).isTrue();
        assertThat(movie1).isEqualTo(movie2);
    }

    @Test
    public void shouldBeEqualWithEqualDbObject() {
        //given
        DBObject dbObject1 = new BasicDBObject("field", "value");
        DBObject dbObject2 = new BasicDBObject("field", "value");

        //when
        MovieWithDefaultEqualsBehaviour movie1 = create(MovieWithDefaultEqualsBehaviour.class, mapper, dbObject1, emptySet());
        MovieWithDefaultEqualsBehaviour movie2 = create(MovieWithDefaultEqualsBehaviour.class, mapper, dbObject2, emptySet());

        //then
        assertThat(dbObject1).isEqualTo(dbObject2);
        assertThat(movie1.equals(movie2)).isTrue();
        assertThat(movie1).isEqualTo(movie2);
    }

    @Test
    public void shouldNotBeEqualWithDifferentDbObjects() {
        //given
        DBObject dbObject1 = new BasicDBObject("field", "value");
        DBObject dbObject2 = new BasicDBObject("differentField", "differentValue");

        //when
        MovieWithDefaultEqualsBehaviour movie1 = create(MovieWithDefaultEqualsBehaviour.class, mapper, dbObject1, emptySet());
        MovieWithDefaultEqualsBehaviour movie2 = create(MovieWithDefaultEqualsBehaviour.class, mapper, dbObject2, emptySet());

        //then
        assertThat(movie1.equals(movie2)).isFalse();
        assertThat(movie1).isNotEqualTo(movie2);
    }

    @Equals(AlwaysTrueEqualsProvider.class)
    private interface ModelAlwaysEqual {

        @DbField("field")
        String getField();

        @DbField("title")
        ModelAlwaysEqual withField(String title);
    }

    public static class AlwaysTrueEqualsProvider implements EqualsProvider<ModelAlwaysEqual> {
        @Override
        public boolean areEqual(ModelAlwaysEqual a, Object b) {
            return true;
        }
    }


    @Test
    public void shouldUseCustomEqualsProviderAndBeAlwaysEqual() {
        //given
        mapper = ReadMapper.map(ModelAlwaysEqual.class);
        DBObject dbObject1 = new BasicDBObject("field", "value");
        DBObject dbObject2 = new BasicDBObject("differentField", "differentValue");

        //when
        ModelAlwaysEqual movie1 = create(ModelAlwaysEqual.class, mapper, dbObject1, emptySet());
        ModelAlwaysEqual movie2 = create(ModelAlwaysEqual.class, mapper, dbObject2, emptySet());

        //then
        assertThat(movie1.equals(movie2)).isTrue();
        assertThat(movie1).isEqualTo(movie2);
    }

    @Equals(TitleEquals.class)
    private interface MovieWithCustomEquals extends MovieWithDefaultEqualsBehaviour {

    }

    public static class TitleEquals implements EqualsProvider<MovieWithCustomEquals> {
        @Override
        public boolean areEqual(MovieWithCustomEquals model, Object obj) {
            if (!isEqualClass(model, obj)) {
                return false;
            }
            MovieWithCustomEquals o = (MovieWithCustomEquals) obj;

            return Objects.equals(model.getTitle(), o.getTitle());
        }
    }

    @Test
    public void shouldBeEqualBasedOnEqualsProvider() {
        //given
        mapper = ReadMapper.map(MovieWithCustomEquals.class);
        DBObject dbObject1 = new BasicDBObject("title", "Star Wars").append("director", "Lucas");
        DBObject dbObject2 = new BasicDBObject("title", "Star Wars").append("director", "George Lucas");

        //when
        MovieWithCustomEquals movie1 = create(MovieWithCustomEquals.class, mapper, dbObject1, emptySet());
        MovieWithCustomEquals movie2 = create(MovieWithCustomEquals.class, mapper, dbObject2, emptySet());

        //then
        assertThat(movie1.equals(movie2)).isTrue();
        assertThat(movie1).isEqualTo(movie2);
    }

    @Test
    public void shouldNotBeEqualBasedOnEqualsProvider() {
        //given
        mapper = ReadMapper.map(MovieWithCustomEquals.class);
        DBObject dbObject1 = new BasicDBObject("title", "Star Wars").append("director", "Lucas");
        DBObject dbObject2 = new BasicDBObject("title", "Indiana Jones").append("director", "Lucas");

        //when
        MovieWithCustomEquals movie1 = create(MovieWithCustomEquals.class, mapper, dbObject1, emptySet());
        MovieWithCustomEquals movie2 = create(MovieWithCustomEquals.class, mapper, dbObject2, emptySet());

        //then
        assertThat(movie1.equals(movie2)).isFalse();
        assertThat(movie1).isNotEqualTo(movie2);
    }

}
