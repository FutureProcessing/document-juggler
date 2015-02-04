package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.futureprocessing.mongojuggler.helper.Sets.asSet;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class SetReadCommandTest {

    private static final String FIELD = "testField";
    private static final List<String> VALUES = asList("value1", "value2");

    private ReadCommand command = new SetReadCommand(FIELD);

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
    @SuppressWarnings("unchecked")
    public void shouldReadValueWhenNoProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, VALUES);
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(Set.class);
        assertThat((Set)value).containsAll(VALUES);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReadValueWhenProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, VALUES);
        Set<String> queriedFields = asSet(FIELD);

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(Set.class);
        assertThat((Set)value).containsAll(VALUES);
    }
}
