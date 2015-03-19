package com.futureprocessing.documentjuggler.annotation;


import com.futureprocessing.documentjuggler.exception.AnnotationCollisionException;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static com.futureprocessing.documentjuggler.assertions.JugglerAssertions.failExpectedException;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationReaderTest {

    @DbField("custom")
    @Retention(RUNTIME)
    @Target(METHOD)
    private @interface Custom {

    }

    private interface LevelsExample {

        @Custom
        @DbField("test")
        String test();
    }

    @Test
    public void shouldReadCloserAnnotationIfDuplicated() throws NoSuchMethodException {
        // given
        AnnotationReader reader = AnnotationReader.from(LevelsExample.class.getMethod("test"));

        // when
        DbField field = reader.read(DbField.class);

        // then
        assertThat(field.value()).isEqualTo("test");
    }

    @Test
    public void shouldReadAllAnnotationsOfGivenTypeFromDifferentLevels() throws NoSuchMethodException {
        // given
        AnnotationReader reader = AnnotationReader.from(LevelsExample.class.getMethod("test"));

        // when
        List<DbField> fields = reader.readAll(DbField.class);

        // then
        assertThat(fields.stream().map(field -> field.value()).collect(toList())).containsExactly("test", "custom");

    }

    @DbField("anotherCustom")
    @Retention(RUNTIME)
    @Target(METHOD)
    private @interface AnotherCustom {

    }

    private interface Collision {

        @Custom
        @AnotherCustom
        String test();
    }

    @Test
    public void shouldThrowAnnotationCollisionExceptionWhenTwoAnnotationOnSameLevelWithDifferentValue() throws NoSuchMethodException {
        // given
        AnnotationReader reader = AnnotationReader.from(Collision.class.getMethod("test"));

        try {
            // when
            reader.read(DbField.class);
        } catch (AnnotationCollisionException ex) {
            // then
            assertThat(ex.getAnnotationClass()).isEqualTo(DbField.class);
            return;
        }

        failExpectedException();
    }

    @Test
    public void shouldReadAllAnnotationsOfGivenTypeFromSingleLevel() throws NoSuchMethodException {
        // given
        AnnotationReader reader = AnnotationReader.from(Collision.class.getMethod("test"));

        // when
        List<DbField> fields = reader.readAll(DbField.class);

        // then
        assertThat(fields.stream().map(field -> field.value()).collect(toList())).containsExactly("custom", "anotherCustom");

    }

    @DbField("custom")
    @Retention(RUNTIME)
    @Target(METHOD)
    private @interface SameCustom {

    }

    private interface Duplicated {

        @Custom
        @SameCustom
        String test();
    }

    @Test
    public void shouldReadAnnotationIfDuplicatedAndEqual() throws NoSuchMethodException {
        // given
        AnnotationReader reader = AnnotationReader.from(Duplicated.class.getMethod("test"));

        // when
        DbField field = reader.read(DbField.class);

        // then
        assertThat(field.value()).isEqualTo("custom");
    }
}
