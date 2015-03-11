package com.futureprocessing.documentjuggler.read.command;


import com.futureprocessing.documentjuggler.example.cars.model.Luggage;
import com.futureprocessing.documentjuggler.exception.FieldNotLoadedException;
import com.futureprocessing.documentjuggler.read.ReaderMapper;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.Set;

import static com.futureprocessing.documentjuggler.helper.Sets.asSet;
import static java.lang.reflect.Proxy.isProxyClass;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class EmbeddedSetReadCommandTest {

    private static final String FIELD = "testField";
    private static final Class<?> EMBEDDED_TYPE = Luggage.class;
    private static final ReaderMapper mapper = new ReaderMapper(EMBEDDED_TYPE);

    private ReadCommand command = new EmbeddedSetReadCommand(FIELD, EMBEDDED_TYPE, mapper);

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
    public void shouldReadValueFromSetWhenNoProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(new BasicDBObject()));
        Set<String> queriedFields = emptySet();

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(Set.class);

        Set set = (Set) value;
        assertThat(set).hasSize(1);
        set.forEach(el -> assertThat(isProxyClass(el.getClass())).isTrue());
    }

    @Test
    public void shouldReadValueFromSetWhenProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(new BasicDBObject()));
        Set<String> queriedFields = asSet(FIELD);

        // when
        Object value = command.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(Set.class);

        Set set = (Set) value;
        assertThat(set).hasSize(1);
        set.forEach(el -> assertThat(isProxyClass(el.getClass())).isTrue());
    }

    @Test
    public void shouldReturnUnmodifiableSet() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(new BasicDBObject()));
        Set<String> queriedFields = emptySet();

        // when
        Set value = (Set) command.read(document, queriedFields);

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
