package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.update.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.update.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PushSingleUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE = "SomeValue";

    private UpdateCommand command = new PushSingleUpdateCommand(FIELD);

    @Test
    public void shouldPush() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{VALUE});

        // then
        BasicDBObject expected = new BasicDBObject("$push",
                                                   new BasicDBObject(FIELD, new BasicDBObject("$each", asList(VALUE))));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

}
