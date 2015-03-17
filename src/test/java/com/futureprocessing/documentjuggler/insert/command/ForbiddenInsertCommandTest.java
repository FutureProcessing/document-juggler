package com.futureprocessing.documentjuggler.insert.command;

import com.futureprocessing.documentjuggler.exception.ForbiddenMethodException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.fail;


public class ForbiddenInsertCommandTest {

    @Test
    public void shouldThrowUnsupportedActionExceptionWhenInserting() throws NoSuchMethodException {
        // given
        InsertCommand command = new ForbiddenInsertCommand(Object.class.getMethod("equals", Object.class));
        BasicDBObject document = new BasicDBObject();

        try {
            // when
            command.insert(document, new Object[]{});
        } catch (ForbiddenMethodException ex) {
            // then
            return;
        }

        fail("Exception expected");
    }
}
