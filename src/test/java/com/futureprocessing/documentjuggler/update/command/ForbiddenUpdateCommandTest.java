package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import org.junit.Test;

import static com.futureprocessing.documentjuggler.Context.UPDATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class ForbiddenUpdateCommandTest {

    @Test
    public void shouldThrowForbiddenActionExceptionWhenUpdating() throws NoSuchMethodException {
        // given
        ForbiddenUpdateCommand command = new ForbiddenUpdateCommand(Object.class.getMethod("equals", Object.class));

        try {
            // when
            command.update(null, null);
        } catch (ForbiddenOperationException ex) {
            // then
            assertThat(ex.getContext()).isEqualTo(UPDATE);
            return;
        }

        fail("Exception expected");
    }
}
