package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.example.model.Engine;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.Set;

import static com.futureprocessing.mongojuggler.helper.Sets.asSet;
import static java.lang.reflect.Proxy.isProxyClass;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class EmbeddedReadCommandTest {

    private static final String FIELD = "testField";
    private static final Class<?> EMBEDDED_TYPE = Engine.class;
    private static final ReadMapper mapper  = new ReadMapper();

    private ReadCommand command = new EmbeddedReadCommand(FIELD, EMBEDDED_TYPE, mapper);

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
        BasicDBObject document = new BasicDBObject(FIELD, new BasicDBObject());
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(EMBEDDED_TYPE);
        assertThat(isProxyClass(value.getClass())).isTrue();
    }

    @Test
    public void shouldReadValueWhenProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, new BasicDBObject());
        Set<String> queriedFields = asSet(FIELD);

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(EMBEDDED_TYPE);
        assertThat(isProxyClass(value.getClass())).isTrue();
    }

    @Test
    public void shouldReturnNullIfFieldIsEmpty() {
        // given
        BasicDBObject document = new BasicDBObject();
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isNull();
    }

}
