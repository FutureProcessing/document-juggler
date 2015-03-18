package com.futureprocessing.documentjuggler.insert.command;

import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static com.futureprocessing.documentjuggler.Context.INSERT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class ForbiddenInsertCommandTest {

    @Test
    public void shouldThrowForbiddenActionExceptionWhenInserting() throws NoSuchMethodException {
        // given
        InsertCommand command = new ForbiddenInsertCommand(Object.class.getMethod("equals", Object.class));
        BasicDBObject document = new BasicDBObject();

        try {
            // when
            command.insert(document, new Object[]{});
        } catch (ForbiddenOperationException ex) {
            // then
            assertThat(ex.getContext()).isEqualTo(INSERT);
            return;
        }

        fail("Exception expected");
    }
}
