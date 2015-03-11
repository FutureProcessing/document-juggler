package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.update.RootUpdateBuilder;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IncreaseUpdateCommandTest {

    private static final String FIELD = "testField";
    private static final int VALUE = 21;

    private UpdateCommand command = new IncrementUpdateCommand(FIELD);

    @Test
    public void shouldIncValue() {
        // given
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{VALUE});

        // then
        BasicDBObject expected = new BasicDBObject("$inc", new BasicDBObject(FIELD, VALUE));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }

}
