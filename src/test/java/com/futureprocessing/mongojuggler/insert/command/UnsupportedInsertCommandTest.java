package com.futureprocessing.mongojuggler.insert.command;

import com.futureprocessing.mongojuggler.exception.validation.UnsupportedMethodException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.fail;


public class UnsupportedInsertCommandTest {

    private InsertCommand command = new UnsupportedInsertCommand(null);

    @Test
    public void shouldThrowUnsupportedActionExceptionWhenInserting() {
        // given
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
