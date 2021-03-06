package com.futureprocessing.documentjuggler.read.command;


import com.futureprocessing.documentjuggler.exception.FieldNotLoadedException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.Set;

import static com.futureprocessing.documentjuggler.helper.Sets.asSet;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class BasicReadCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE = "SomeValue";

    private ReadCommand command = new BasicReadCommand(FIELD);

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
    public void shouldReadValueListOfStrings() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(VALUE));
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isEqualTo(asList(VALUE));
    }
}
