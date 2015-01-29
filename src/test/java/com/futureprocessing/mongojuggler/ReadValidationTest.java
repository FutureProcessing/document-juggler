package com.futureprocessing.mongojuggler;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
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
public class ReadValidationTest {

    @Mock
    private MongoDBProvider dbProvider;

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface() {
        // given

        try {
            // when
            new Repository<>(NotInterface.class, Empty.class, Empty.class, dbProvider);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        fail();
    }

    private class NotInterface {

        @Id
        String getId() {
            return null;
        }
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            new Repository<>(UnknownFieldQuery.class, Empty.class, Empty.class, dbProvider);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(UnknownFieldQuery.class.getMethod("getId"));
            return;
        }

        fail();
    }

    private interface UnknownFieldQuery {

        String getId();
    }

    @Test
    public void shouldThrowInvalidArgumentsExceptionWhenReadMethodHasArguments() throws Exception {
        // given

        try {
            // when
            new Repository<>(ReaderWithArguments.class, Empty.class, Empty.class, dbProvider);
        } catch (InvalidArgumentsException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(ReaderWithArguments.class.getMethod("getTest", String.class));
            return;
        }

        fail();
    }

    private interface ReaderWithArguments {

        @DbField("Test")
        String getTest(String arg);
    }

}
