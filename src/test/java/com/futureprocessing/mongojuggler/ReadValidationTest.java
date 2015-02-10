package com.futureprocessing.mongojuggler;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ReadValidationTest {

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface() {
        // given

        try {
            // when
            new ReadMapper(NotInterface.class);
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
            new ReadMapper(UnknownFieldQuery.class);
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
            new ReadMapper(ReaderWithArguments.class);
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
