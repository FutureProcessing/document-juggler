package com.futureprocessing.mongojuggler.read.command;


import com.futureprocessing.mongojuggler.example.model.Luggage;
import com.futureprocessing.mongojuggler.exception.FieldNotLoadedException;
import com.futureprocessing.mongojuggler.read.ReadMapper;
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

public class EmbeddedCollectionReadCommandTest {

    private static final String FIELD = "testField";
    private static final Class<?> EMBEDDED_TYPE = Luggage.class;
    private static final ReadMapper mapper = new ReadMapper(EMBEDDED_TYPE);

    private ReadCommand listCommand = EmbeddedCollectionReadCommand.forList(FIELD, EMBEDDED_TYPE, mapper);
    private ReadCommand setCommand = EmbeddedCollectionReadCommand.forSet(FIELD, EMBEDDED_TYPE, mapper);

    @Test
    public void shouldThrowFieldNotLoadedExceptionWhenAccessingNotLoadedField() {
        // given
        BasicDBObject document = new BasicDBObject();
        Set<String> queriedFields = asSet("otherField");

        // when
        try {
            listCommand.read(document, queriedFields);
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
        Object value = listCommand.read(document, queriedFields);

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
        Object value = listCommand.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(List.class);

        List list = (List) value;
        assertThat(list).hasSize(1);
        assertThat(isProxyClass(list.get(0).getClass())).isTrue();
    }

    @Test
    public void shouldReadValueFromSetWhenNoProjectionSpecified() {
        // given
        BasicDBObject document = new BasicDBObject(FIELD, asList(new BasicDBObject()));
        Set<String> queriedFields = emptySet();

        // when
        Object value = setCommand.read(document, queriedFields);

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
        Object value = setCommand.read(document, queriedFields);

        // then
        assertThat(value).isInstanceOf(Set.class);

        Set set = (Set) value;
        assertThat(set).hasSize(1);
        set.forEach(el -> assertThat(isProxyClass(el.getClass())).isTrue());
    }

    @Test
    public void shouldReturnNullIfFieldIsEmpty() {
        // given
        BasicDBObject document = new BasicDBObject();
        Set<String> queriedFields = emptySet();

        // when
        Object value = listCommand.read(document, queriedFields);

        // then
        assertThat(value).isNull();
    }

}
