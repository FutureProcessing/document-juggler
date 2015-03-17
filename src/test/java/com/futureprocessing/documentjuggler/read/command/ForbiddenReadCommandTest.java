package com.futureprocessing.documentjuggler.read.command;

import com.futureprocessing.documentjuggler.exception.ForbiddenActionException;
import org.junit.Test;

import static com.futureprocessing.documentjuggler.Context.READ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class ForbiddenReadCommandTest {

    @Test
    public void shouldThrowForbiddenActionExceptionWhenReading() throws NoSuchMethodException {
        // given
        ReadCommand command = new ForbiddenReadCommand(Object.class.getMethod("equals", Object.class));

        try {
            // when
            command.read(null, null);
        } catch (ForbiddenActionException ex) {
            // then
            assertThat(ex.getContext()).isEqualTo(READ);
            return;
        }

        fail("Exception expected");
    }
}
