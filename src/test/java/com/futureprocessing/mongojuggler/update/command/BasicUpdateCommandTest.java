package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.update.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.update.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE = "SomeValue";

    private UpdateCommand command = new BasicUpdateCommand(FIELD, false);

    @Test
    public void shouldSetValue() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{VALUE});

        // then
        BasicDBObject expected = new BasicDBObject("$set", new BasicDBObject(FIELD, VALUE));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

    @Test
    public void shouldSetValueToNull() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{null});

        // then
        BasicDBObject expected = new BasicDBObject("$set", new BasicDBObject(FIELD, null));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

    @Test
    public void shouldUnsetIfValueIsNull() {
        // given
        UpdateCommand command = new BasicUpdateCommand(FIELD, true);
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{null});

        // then
        BasicDBObject expected = new BasicDBObject("$unset", new BasicDBObject(FIELD, null));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }
}
