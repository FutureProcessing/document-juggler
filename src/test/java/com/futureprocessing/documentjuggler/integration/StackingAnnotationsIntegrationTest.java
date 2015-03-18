package com.futureprocessing.documentjuggler.integration;


import com.futureprocessing.documentjuggler.BaseRepository;
import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Forbidden;
import com.futureprocessing.documentjuggler.annotation.Id;
import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.futureprocessing.documentjuggler.Context.UPDATE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class StackingAnnotationsIntegrationTest extends BaseIntegrationTest {

    private static BaseRepository<Model> repo;

    @DbField("custom")
    @Forbidden(UPDATE)
    @Retention(RUNTIME)
    @Target(METHOD)
    private @interface CustomField {

    }

    @CollectionName("stackingTest")
    private interface Model {

        @Id
        public String getId();

        @Id
        public Model withId(String id);

        @CustomField
        public Model withCustom(String value);

        @CustomField
        public String getCustom();

    }

    @BeforeClass
    public static void init() {
        repo = new BaseRepository<>(db(), Model.class);
    }

    @Test
    public void idAnnotationShouldWorkProperly() {
        // given
        String insertedId = repo.insert(model -> model.withCustom("Test"));

        // when
        String id = repo.find(model -> model.withId(insertedId)).first().getId();

        // then
        assertThat(id).isInstanceOf(String.class);
        assertThat(id).isEqualTo(insertedId);


    }

    @Test
    public void customAnnotationShouldWorkProperly() {
        // given
        String id = repo.insert(model -> model.withCustom("Test"));

        // when
        String value = repo.find(model -> model.withId(id)).first().getCustom();
        try {
            repo.find(model -> model.withId(id)).update(model -> model.withCustom("New"));
        } catch (ForbiddenOperationException ex) {
            // then
            assertThat(value).isEqualTo("Test");
            return;
        }

        fail("Expected Exception");
    }
}
