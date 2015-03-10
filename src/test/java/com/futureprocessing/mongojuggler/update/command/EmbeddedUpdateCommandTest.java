package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.MappingMode;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.update.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.update.UpdateBuilder;
import com.futureprocessing.mongojuggler.update.UpdaterMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.function.Consumer;

import static com.futureprocessing.mongojuggler.MappingMode.*;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String EMBEDDED_FIELD = "eField";
    private static final String VALUE = "SomeValue";

    private UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);
    private UpdateCommand command = new EmbeddedUpdateCommand(FIELD, Update.class, mapper);

    @Test
    public void shouldSetValue() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{new Consumer<Update>() {

            @Override
            public void accept(Update update) {
                update.withValue(VALUE);
            }
        }});

        // then
        BasicDBObject expected = new BasicDBObject("$set", new BasicDBObject(FIELD + "." + EMBEDDED_FIELD, VALUE));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

    private interface Update {

        @DbField(EMBEDDED_FIELD)
        Update withValue(String value);
    }


}
