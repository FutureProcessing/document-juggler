package com.futureprocessing.documentjuggler.fieldNames;

import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.assertions.JugglerAssertions.failExpectedException;
import static org.assertj.core.api.Assertions.assertThat;

public class FieldNameExtractorTest {

    @CollectionName("movie")
    private interface Movie {
        String getTitle();

        void setTitle(String title);

        Movie withTitle(String title);

        String title();

        String getLongNameWithUppercases();

        @DbField("")
        String emptyField();

        String get();

        String set(String value);

        String with(String value);
    }

    @Test
    public void shouldGetDbFieldNameFromGetterName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("getTitle");

        //when
        String methodName = FieldNameExtractor.getFieldName(method);

        //then
        assertThat(methodName).isEqualTo("title");
    }

    @Test
    public void shouldCreateDbFieldNameFromSetterName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("setTitle", String.class);

        //when

        //then
        assertThat(FieldNameExtractor.getFieldName(method)).isEqualTo("title");
    }

    @Test
    public void shouldCreateDbFieldNameFromWithMethodName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("withTitle", String.class);

        //when

        //then
        assertThat(FieldNameExtractor.getFieldName(method)).isEqualTo("title");
    }

    @Test
    public void shouldCreateDbFieldNameFromLongMethodName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("getLongNameWithUppercases");

        //when

        //then
        assertThat(FieldNameExtractor.getFieldName(method)).isEqualTo("longNameWithUppercases");
    }

    @Test
    public void shouldNotCreateDbFieldNameFromMethodName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("title");

        //when
        try {
            FieldNameExtractor.getFieldName(method);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(method);
            return;
        }

        Assert.fail();
    }

    @Test
    public void shouldThrowExeptionForEmptyFieldName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("emptyField");

        //when
        try {
            FieldNameExtractor.getFieldName(method);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(method);
            return;
        }

        failExpectedException();
    }

    @Test
    public void shouldThrowExeptionForGetOnly() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("get");

        //when
        try {
            FieldNameExtractor.getFieldName(method);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(method);
            return;
        }

        failExpectedException();
    }

    @Test
    public void shouldThrowExeptionForSetOnly() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("set", String.class);

        //when
        try {
            FieldNameExtractor.getFieldName(method);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(method);
            return;
        }

        failExpectedException();
    }

    @Test
    public void shouldThrowExeptionForWithOnly() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("with", String.class);

        //when
        try {
            FieldNameExtractor.getFieldName(method);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(method);
            return;
        }

        failExpectedException();
    }
}
