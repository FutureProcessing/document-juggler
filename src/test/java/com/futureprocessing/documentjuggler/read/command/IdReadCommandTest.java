package com.futureprocessing.documentjuggler.read.command;


import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.Set;

import static com.futureprocessing.documentjuggler.helper.Sets.asSet;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

public class IdReadCommandTest {

    private static final String FIELD = "_id";
    private static final ObjectId VALUE = new ObjectId();

    private ReadCommand command = new IdReadCommand();

    @Test
    public void shouldReadValueWhenNoProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, VALUE);
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isEqualTo(VALUE.toHexString());
    }

    @Test
    public void shouldReadValueWhenProjectionWithAnyFieldsSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, VALUE);
        Set<String> queriedFields = asSet("OtherField");

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isEqualTo(VALUE.toHexString());
    }
}
