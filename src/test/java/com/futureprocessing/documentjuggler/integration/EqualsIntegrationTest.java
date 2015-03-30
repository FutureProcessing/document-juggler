package com.futureprocessing.documentjuggler.integration;

import com.futureprocessing.documentjuggler.Repository;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Equals;
import com.futureprocessing.documentjuggler.commons.EqualsProvider;
import com.futureprocessing.documentjuggler.model.DefaultModel;
import org.junit.Test;

import java.util.Objects;
import java.util.function.Consumer;

import static com.futureprocessing.documentjuggler.commons.EqualsProvider.BaseEquals.isEqualClass;
import static org.assertj.core.api.Assertions.assertThat;

public class EqualsIntegrationTest extends BaseIntegrationTest {

    private interface MovieWithDefaultEquals extends DefaultModel<MovieWithDefaultEquals> {
        @DbField("title")
        String getTitle();

        @DbField("title")
        MovieWithDefaultEquals withTitle(String title);

        @DbField("director")
        @DbEmbeddedDocument
        Person getDirector();

        @DbField("director")
        @DbEmbeddedDocument
        MovieWithDefaultEquals withDirector(Consumer<Person> director);
    }

    private interface Person {
        @DbField("name")
        String getName();

        @DbField("name")
        Person withName(String name);
    }

    @Test
    public void oneDocumentShouldBeEqualWithDefaultBehaviour() {
        //given
        Repository<MovieWithDefaultEquals> repository = new Repository<>(db().getCollection("col"), MovieWithDefaultEquals.class);
        final String titanicId = repository.insert(m -> m.withTitle("Titanic"));

        //when
        MovieWithDefaultEquals first = repository.find(m -> m.withId(titanicId)).first();
        MovieWithDefaultEquals second = repository.find(m -> m.withId(titanicId)).first();

        //then
        assertThat(first).isEqualTo(second);
    }

    @Test
    public void oneDocumentShouldBeEqualWithDefaultBehaviourAndEmbeddedDocument() {
        //given
        Repository<MovieWithDefaultEquals> repository = new Repository<>(db().getCollection("col"), MovieWithDefaultEquals.class);
        final String titanicId = repository.insert(m -> m.withTitle("Titanic")
                .withDirector(d -> d.withName("Cameron")));

        //when
        MovieWithDefaultEquals first = repository.find(m -> m.withId(titanicId)).first();
        MovieWithDefaultEquals second = repository.find(m -> m.withId(titanicId)).first();

        //then
        assertThat(first).isEqualTo(second);
        assertThat(first.getDirector().getName()).isEqualTo("Cameron");
    }

    @Test
    public void twoDocumentsShouldNotBeEqualWithDefaultBehaviour() {
        //given
        Repository<MovieWithDefaultEquals> repository = new Repository<>(db().getCollection("col"), MovieWithDefaultEquals.class);
        final String titanicId = repository.insert(m -> m.withTitle("Titanic").withDirector(d -> d.withName("Cameron")));
        final String avatarId = repository.insert(m -> m.withTitle("Avatar").withDirector(d -> d.withName("Cameron")));

        //when
        MovieWithDefaultEquals first = repository.find(m -> m.withId(titanicId)).first();
        MovieWithDefaultEquals second = repository.find(m -> m.withId(avatarId)).first();

        //then
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void twoDocumentsShouldNotBeEqualWithDefaultBehaviourWhenHaveDifferentId() {
        //given
        Repository<MovieWithDefaultEquals> repository = new Repository<>(db().getCollection("col"), MovieWithDefaultEquals.class);
        final String titanicId = repository.insert(m -> m.withTitle("Titanic").withDirector(d -> d.withName("Cameron")));
        final String titanicTwoId = repository.insert(m -> m.withTitle("Titanic").withDirector(d -> d.withName("Cameron")));

        //when
        MovieWithDefaultEquals first = repository.find(m -> m.withId(titanicId)).first();
        MovieWithDefaultEquals second = repository.find(m -> m.withId(titanicTwoId)).first();

        //then
        assertThat(first).isNotEqualTo(second);
    }

    @Equals(TitleEquals.class)
    private interface MovieWithCustomEquals extends MovieWithDefaultEquals{

    }

    public static class TitleEquals implements EqualsProvider<MovieWithCustomEquals> {
        @Override
        public boolean areEqual(MovieWithCustomEquals model, Object obj) {
            if (!isEqualClass(model, obj)){
                return false;
            }
            MovieWithCustomEquals o = (MovieWithCustomEquals) obj;
            return Objects.equals(model.getTitle(), o.getTitle());
        }
    }

    @Test
    public void twoDocumentsShouldBeEqualWithCustomEquals() {
        //given
        Repository<MovieWithCustomEquals> repository = new Repository<>(db().getCollection("col"), MovieWithCustomEquals.class);
        final String titanicId = repository.insert(m -> m.withTitle("Titanic").withDirector(d -> d.withName("Cameron")));
        final String titanicTwoId = repository.insert(m -> m.withTitle("Titanic").withDirector(d -> d.withName("Cameron")));

        //when
        MovieWithDefaultEquals first = repository.find(m -> m.withId(titanicId)).first();
        MovieWithDefaultEquals second = repository.find(m -> m.withId(titanicTwoId)).first();

        //then
        assertThat(first).isEqualTo(second);
    }

}
