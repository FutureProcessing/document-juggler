package com.futureprocessing.documentjuggler.fieldNames;

import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.commons.Validator;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ValidateFiledsTest {

    @CollectionName("movie")
    private interface Movie {
        String getTitle();

        @DbField("movie_author")
        String getAuthor();

        String title();
    }

    @Test
    public void shouldBeValidInGetterSetterWithIfDbFieldAnnotationNotExist() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("getTitle");

        //when
        //then
        Validator.validateField(method);
    }

    @Test
    public void shouldBeValidIfDbFieldAnnotationExist() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("getAuthor");

        //when
        //then
        Validator.validateField(method);
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("title");

        //when
        try {
            Validator.validateField(method);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(Movie.class.getMethod("title"));
            return;
        }

        fail();
    }
}
