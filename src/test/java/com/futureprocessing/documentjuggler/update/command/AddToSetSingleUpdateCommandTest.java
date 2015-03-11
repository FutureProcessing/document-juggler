package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.update.RootUpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AddToSetSingleUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE = "SomeValue";

    private UpdateCommand command = new AddToSetSingleUpdateCommand(FIELD);

    @Test
    public void shouldAddToSet() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{VALUE});

        // then
        BasicDBObject expected = new BasicDBObject("$addToSet", new BasicDBObject(FIELD, new BasicDBObject("$each", asList(VALUE))));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

}
