package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.Set;

import static com.futureprocessing.mongojuggler.helper.Sets.asSet;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;

public class BooleanReadCommandTest {

    private static final String FIELD = "testField";
    private static final boolean VALUE = true;

    private ReadCommand command = new BooleanReadCommand(FIELD);

    @Test
    public void shouldThrowFieldNotLoadedExceptionWhenAccessingNotLoadedField() {
        // given
        BasicDBObject document = new BasicDBObject();
        Set<String> queriedFields = asSet("otherField");

        // when
        try {
            command.read(document, queriedFields);
        } catch (FieldNotLoadedException e) {
            // then
            return;
        }

        fail("Should thrown exception");
    }

    @Test
    public void shouldReadValueWhenNoProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, VALUE);
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isEqualTo(VALUE);
    }

    @Test
    public void shouldReadValueWhenProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, VALUE);
        Set<String> queriedFields = asSet(FIELD);

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isEqualTo(VALUE);
    }

    @Test
    public void booleanValueShouldBeFalseWhenNotPresent() {
        // given
        BasicDBObject document = new BasicDBObject();
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isEqualTo(false);
    }
}
