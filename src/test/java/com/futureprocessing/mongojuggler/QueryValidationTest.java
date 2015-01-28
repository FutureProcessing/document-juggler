package com.futureprocessing.mongojuggler;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.exception.validation.InvalidReturnValueException;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.mongojuggler.helper.Empty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class QueryValidationTest {

    @Mock
    private MongoDBProvider dbProvider;

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfQueryIsNotInterface() {
        // given

        try {
            // when
            new Repository<>(Empty.class, Empty.class, NoInterface.class, dbProvider);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NoInterface.class);
            return;
        }

        fail();
    }

    private class NoInterface {

        NoInterface withId(String id) {
            return null;
        }
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            new Repository<>(Empty.class, Empty.class, UnknownFieldQuery.class, dbProvider);
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
            new Repository<>(Empty.class, Empty.class, InvalidReturnTypeQuery.class, dbProvider);
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
            new Repository<>(Empty.class, Empty.class, NoArgumentQuery.class, dbProvider);
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
            new Repository<>(Empty.class, Empty.class, TwoArgumentQuery.class, dbProvider);
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
