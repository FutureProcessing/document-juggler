package com.futureprocessing.mongojuggler.insert;


import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Push;
import com.futureprocessing.mongojuggler.helper.Empty;
import com.futureprocessing.mongojuggler.insert.command.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class InserterMapperTest {

    @Test
    public void shouldReturnEmbeddedInsertCommand() throws Exception {
        // given
        Method method = Insert.class.getMethod("embedded", Consumer.class);

        // when
        InserterMapper mapper = new InserterMapper(Insert.class);

        // then
        InsertCommand command = mapper.get(Insert.class).get(method);
        assertThat(command).isInstanceOf(EmbeddedInsertCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedVarArgInsertCommand() throws Exception {
        // given
        Method method = Insert.class.getMethod("embeddedVarArg", Consumer[].class);

        // when
        InserterMapper mapper = new InserterMapper(Insert.class);

        // then
        InsertCommand command = mapper.get(Insert.class).get(method);
        assertThat(command).isInstanceOf(EmbeddedVarArgInsertCommand.class);
    }

    @Test
    public void shouldReturnBasicInsertCommand() throws Exception {
        // given
        Method method = Insert.class.getMethod("value", String.class);

        // when
        InserterMapper mapper = new InserterMapper(Insert.class);

        // then
        InsertCommand command = mapper.get(Insert.class).get(method);
        assertThat(command).isInstanceOf(BasicInsertCommand.class);
    }

    @Test
    public void shouldReturnUnsupportedInsertCommandForAddToSetAnnotation() throws Exception {
        // given
        Method method = Insert.class.getMethod("unsupportedAddToSet", String.class);

        // when
        InserterMapper mapper = new InserterMapper(Insert.class);

        // then
        InsertCommand command = mapper.get(Insert.class).get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }

    @Test
    public void shouldReturnUnsupportedInsertCommandForPushAnnotation() throws Exception {
        // given
        Method method = Insert.class.getMethod("unsupportedPush", String.class, String.class);

        // when
        InserterMapper mapper = new InserterMapper(Insert.class);

        // then
        InsertCommand command = mapper.get(Insert.class).get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }


    private interface Insert {

        @DbField("embedded")
        @DbEmbeddedDocument
        Insert embedded(Consumer<Empty> consumer);

        @DbField("embedded")
        @DbEmbeddedDocument
        Insert embeddedVarArg(Consumer<Empty>... consumers);

        @DbField("value")
        Insert value(String value);

        @DbField("set")
        @AddToSet
        Insert unsupportedAddToSet(String value);

        @DbField("list")
        @Push
        Insert unsupportedPush(String value1, String value2);

    }
}
