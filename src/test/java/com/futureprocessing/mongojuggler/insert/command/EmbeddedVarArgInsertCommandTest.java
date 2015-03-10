package com.futureprocessing.mongojuggler.insert.command;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.insert.InserterMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedVarArgInsertCommandTest {

    private static final String FIELD = "testField";
    private static final String EMBEDDED_FIELD = "embeddedField";
    private static final String VALUE1 = "SomeValue";
    private static final String VALUE2 = "SomeValue";

    private InserterMapper mapper = new InserterMapper(Insert.class);
    private InsertCommand command = new EmbeddedVarArgInsertCommand(FIELD, Insert.class, mapper);

    @Test
    public void shouldInsertSingleValue() {
        // given
        BasicDBObject document = new BasicDBObject();

        // when
        command.insert(document, new Object[]{new Consumer[]{new Consumer<Insert>() {
            @Override
            public void accept(Insert insert) {
                insert.withValue(VALUE1);
            }
        }}});

        // then
        assertThat(document).isEqualTo(new BasicDBObject(FIELD, asList(new BasicDBObject(EMBEDDED_FIELD, VALUE1))));
    }

    @Test
    public void shouldInsertManyValue() {
        // given
        BasicDBObject document = new BasicDBObject();

        // when
        command.insert(document, new Object[]{new Consumer[]{
                new Consumer<Insert>() {
                    @Override
                    public void accept(Insert insert) {
                        insert.withValue(VALUE1);
                    }
                },
                new Consumer<Insert>() {
                    @Override
                    public void accept(Insert insert) {
                        insert.withValue(VALUE2);
                    }
                }
        }});

        // then
        assertThat(document).isEqualTo(new BasicDBObject(FIELD, asList(
                new BasicDBObject(EMBEDDED_FIELD, VALUE1),
                new BasicDBObject(EMBEDDED_FIELD, VALUE2))
        ));
    }

    private interface Insert {

        @DbField(EMBEDDED_FIELD)
        Insert withValue(String value);
    }
}
