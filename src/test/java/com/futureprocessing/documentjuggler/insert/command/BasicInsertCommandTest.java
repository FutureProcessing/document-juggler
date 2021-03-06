package com.futureprocessing.documentjuggler.insert.command;

import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicInsertCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE = "SomeValue";

    private InsertCommand command = new BasicInsertCommand(FIELD);

    @Test
    public void shouldInsertValue() {
        // given
        BasicDBObject document = new BasicDBObject();

        // when
        command.insert(document, new Object[]{VALUE});

        // then
        assertThat(document).isEqualTo(new BasicDBObject(FIELD, VALUE));
    }
}
