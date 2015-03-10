package com.futureprocessing.mongojuggler.insert.command;

import com.futureprocessing.mongojuggler.exception.validation.UnsupportedMethodException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.fail;


public class UnsupportedInsertCommandTest {

    @Test
    public void shouldThrowUnsupportedActionExceptionWhenInserting() throws NoSuchMethodException {
        // given
        InsertCommand command = new UnsupportedInsertCommand(Object.class.getMethod("equals", Object.class));
        BasicDBObject document = new BasicDBObject();

        try {
            // when
            command.insert(document, new Object[]{});
        } catch (UnsupportedMethodException ex) {
            // then
            return;
        }

        fail("Exception expected");
    }
}
