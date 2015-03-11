package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.update.RootUpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdaterMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String EMBEDDED_FIELD = "eField";
    private static final String VALUE = "SomeValue";

    private UpdaterMapper mapper = new UpdaterMapper(Update.class);
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
