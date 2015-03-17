package com.futureprocessing.documentjuggler.query.command;

import com.futureprocessing.documentjuggler.exception.ForbiddenActionException;
import org.junit.Test;

import static com.futureprocessing.documentjuggler.Context.QUERY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class ForbiddenQueryCommandTest {

    @Test
    public void shouldThrowForbiddenActionExceptionWhenQuerying() throws NoSuchMethodException {
        // given
        QueryCommand command = new ForbiddenQueryCommand(Object.class.getMethod("equals", Object.class));

        try {
            // when
            command.query(null, new Object[]{});
        } catch (ForbiddenActionException ex) {
            // then
            assertThat(ex.getContext()).isEqualTo(QUERY);
            return;
        }

        fail("Exception expected");
    }
}
