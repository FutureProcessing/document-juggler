package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.write.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.write.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PushUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE = "SomeValue";

    private UpdateCommand command = new PushUpdateCommand(FIELD);

    @Test
    public void shouldPush() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{VALUE});

        // then
        BasicDBObject expected = new BasicDBObject("$push", new BasicDBObject(FIELD, VALUE));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

}
