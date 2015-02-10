package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.write.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.write.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BooleanUpdateCommandTest {

    private static final String FIELD = "testField";

    private UpdateCommand command = new BooleanUpdateCommand(FIELD);

    @Test
    public void shouldSetIfTrue() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{true});

        // then
        BasicDBObject expected = new BasicDBObject("$set", new BasicDBObject(FIELD, true));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

    @Test
    public void shouldUnsetIfValueIsFalse() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{false});

        // then
        BasicDBObject expected = new BasicDBObject("$unset", new BasicDBObject(FIELD, null));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

    @Test
    public void shouldUnsetIfValueIsNull() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{null});

        // then
        BasicDBObject expected = new BasicDBObject("$unset", new BasicDBObject(FIELD, null));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }
}
