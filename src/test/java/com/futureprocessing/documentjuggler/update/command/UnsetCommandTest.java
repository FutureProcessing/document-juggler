package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.update.RootUpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnsetCommandTest {

    private static final String FIELD_NAME = "testField";

    private UpdateCommand command = new UnsetCommand(FIELD_NAME);

    @Test
    public void shouldUnsetField() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{});

        // then
        BasicDBObject expected = new BasicDBObject("$unset", new BasicDBObject(FIELD_NAME, null));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }
}
