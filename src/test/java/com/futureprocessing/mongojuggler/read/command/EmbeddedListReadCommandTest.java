package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.example.cars.model.Luggage;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.futureprocessing.mongojuggler.read.ReaderMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.futureprocessing.mongojuggler.helper.Sets.asSet;
import static java.lang.reflect.Proxy.isProxyClass;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class EmbeddedListReadCommandTest {

    private static final String FIELD = "testField";
    private static final Class<?> EMBEDDED_TYPE = Luggage.class;
    private static final ReaderMapper mapper = new ReaderMapper(EMBEDDED_TYPE);

    private ReadCommand command = new EmbeddedListReadCommand(FIELD, EMBEDDED_TYPE, mapper);

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
    public void shouldReadValueFromListWhenNoProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(new BasicDBObject()));
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(List.class);

        List list = (List) value;
        assertThat(list).hasSize(1);
        assertThat(isProxyClass(list.get(0).getClass())).isTrue();
    }

    @Test
    public void shouldReadValueFromListWhenProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(new BasicDBObject()));
        Set<String> queriedFields = asSet(FIELD);

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(List.class);

        List list = (List) value;
        assertThat(list).hasSize(1);
        assertThat(isProxyClass(list.get(0).getClass())).isTrue();
    }

    @Test
    public void shouldReturnUnmodifiableList() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(new BasicDBObject()));
        Set<String> queriedFields = emptySet();

        // when
        List value = (List) command.read(document, queriedFields);

        // then
        try {
            value.add(new Object());
        } catch (UnsupportedOperationException ex) {
            return;
        }
        fail("Exception expected");
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
