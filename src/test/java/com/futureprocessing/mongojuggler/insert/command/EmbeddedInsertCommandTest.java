package com.futureprocessing.mongojuggler.insert.command;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.insert.InsertMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedInsertCommandTest {

    private static final String FIELD = "testField";
    private static final String EMBEDDED_FIELD = "embeddedField";
    private static final String VALUE = "SomeValue";

    private InsertMapper mapper = new InsertMapper(Insert.class);
    private InsertCommand command = new EmbeddedInsertCommand(FIELD, Insert.class, mapper);

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
