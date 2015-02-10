package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.write.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.write.UpdateBuilder;
import com.futureprocessing.mongojuggler.write.UpdateMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String EMBEDDED_FIELD = "eField";
    private static final String VALUE = "SomeValue";

    private UpdateMapper mapper = new UpdateMapper();
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
