package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.mongodb.BasicDBObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedInsertCommandTest {

    private static final String FIELD = "testField";
    private static final String EMBEDDED_FIELD = "embeddedField";
    private static final String VALUE = "SomeValue";

    private InsertCommand command = new EmbeddedInsertCommand(FIELD, Insert.class);

    @Test
    public void shouldInsertValue() {
        // given
        BasicDBObject document = new BasicDBObject();

        // when
        command.insert(document, new Object[]{new Consumer<Insert>() {
            @Override
            public void accept(Insert insert) {
                insert.withValue(VALUE);
            }
        }});

        // then
        assertThat(document).isEqualTo(new BasicDBObject(FIELD, new BasicDBObject(EMBEDDED_FIELD, VALUE)));
    }

    private interface Insert {

        @DbField(EMBEDDED_FIELD)
        Insert withValue(String value);
    }
}
