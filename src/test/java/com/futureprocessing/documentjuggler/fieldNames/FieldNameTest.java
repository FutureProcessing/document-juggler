package com.futureprocessing.documentjuggler.fieldNames;

import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldNameTest {

    @CollectionName("movie")
    private interface Movie {
        String getTitle();

        void setTitle(String title);

        Movie withTitle(String title);

        String title();

        String getLongNameWithUppercases();
    }

    @Test
    public void shouldCreateDbFieldNameFromGetterName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("getTitle");

        //when

        //then
        assertThat(FieldNameExtractor.getFieldName(method)).isEqualTo("title");
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
    public void shouldntCreateDbFieldNameFromMethodName() throws NoSuchMethodException {
        //given
        Method method = Movie.class.getMethod("title");

        //when

        //then
        assertThat(FieldNameExtractor.getFieldName(method)).isNull();
    }
}
