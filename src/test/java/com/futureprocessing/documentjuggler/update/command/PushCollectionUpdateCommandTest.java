package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.update.RootUpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PushCollectionUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE_1 = "SomeValue";
    private static final String VALUE_2 = "AnotherValue";

    private UpdateCommand command = new PushCollectionUpdateCommand(FIELD);

    @Test
    public void shouldAddToSet() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{asList(VALUE_1, VALUE_2)});

        // then
        BasicDBObject expected = new BasicDBObject("$push", new BasicDBObject(FIELD, new BasicDBObject("$each", asList(VALUE_1, VALUE_2))));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

}
