package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.exception.UnsupportedActionException;
import com.futureprocessing.mongojuggler.insert.command.InsertCommand;
import com.futureprocessing.mongojuggler.insert.command.UnsupportedInsertCommand;
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
