package com.futureprocessing.mongojuggler;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.exception.validation.InvalidReturnValueException;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.mongojuggler.query.QuerierMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class QueryValidationTest {

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfQueryIsNotInterface() {
        // given

        try {
            // when
            new QuerierMapper(NotInterface.class);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        fail();
    }

    private class NotInterface {

        NotInterface withId(String id) {
            return null;
        }
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            new QuerierMapper(UnknownFieldQuery.class);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(UnknownFieldQuery.class.getMethod("withId", String.class));
            return;
        }

        fail();
    }

    private interface UnknownFieldQuery {

        UnknownFieldQuery withId(String id);
    }

    @Test
    public void shouldThrowInvalidReturnValueExceptionWhenMethodNotReturnStringOrVoid() throws Exception {
        // given

        try {
            // when
            new QuerierMapper(InvalidReturnTypeQuery.class);
        } catch (InvalidReturnValueException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(InvalidReturnTypeQuery.class.getMethod("withId", String.class));
            return;
        }

        fail();
    }

    private interface InvalidReturnTypeQuery {

        @DbField("test")
        Integer withId(String id);
    }

    @Test
    public void shouldThrowInvalidArgumentsExceptionWhenQueryMethodHasNoArguments() throws Exception {
        // given

        try {
            // when
            new QuerierMapper(NoArgumentQuery.class);
        } catch (InvalidArgumentsException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(NoArgumentQuery.class.getMethod("withId"));
            return;
        }

        fail();
    }

    private interface NoArgumentQuery {

        @DbField("Test")
        NoArgumentQuery withId();
    }

    @Test
    public void shouldThrowInvalidArgumentsExceptionWhenQueryMethodHasMoreThanOneArgument() throws Exception {
        // given

        try {
            // when
            new QuerierMapper(TwoArgumentQuery.class);
        } catch (InvalidArgumentsException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(TwoArgumentQuery.class.getMethod("withId", String.class, String.class));
            return;
        }

        fail();
    }

    private interface TwoArgumentQuery {

        @DbField("Test")
        TwoArgumentQuery withId(String id1, String id2);
    }
}
