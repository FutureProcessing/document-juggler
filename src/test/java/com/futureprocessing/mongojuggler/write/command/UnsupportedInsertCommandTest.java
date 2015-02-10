package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.exception.UnsupportedActionException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.fail;


public class UnsupportedInsertCommandTest {

    private InsertCommand command = new UnsupportedInsertCommand();

    @Test
    public void shouldThrowUnsupportedActionExceptionWhenInserting() {
        // given
        BasicDBObject document = new BasicDBObject();

        try {
            // when
            command.insert(document, new Object[]{});
        } catch (UnsupportedActionException ex) {
            // then
            return;
        }

        fail("Exception expected");
    }
}
