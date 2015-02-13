package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.helper.Empty;
import com.futureprocessing.mongojuggler.read.command.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadMapperTest {

    @Test
    public void shouldReturnIdReadCommand() throws Exception {
        // given
        Method method = Read.class.getMethod("id");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(IdReadCommand.class);
    }

    @Test
    public void shouldReturnBooleanReadCommandForPrimitiveBoolean() throws Exception {
        // given
        Method method = Read.class.getMethod("primitiveBoolean");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    public void shouldReturnBooleanReadCommandForBigBoolean() throws Exception {
        // given
        Method method = Read.class.getMethod("bigBoolean");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    public void shouldReturnSetReadCommand() throws Exception {
        // given
        Method method = Read.class.getMethod("set");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(SetReadCommand.class);
    }

    @Test
    public void shouldReturnBasicReadCommand() throws Exception {
        // given
        Method method = Read.class.getMethod("basic");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(BasicReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedReadCommand() throws Exception {
        // given
        Method method = Read.class.getMethod("embedded");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(EmbeddedReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedListReadCommand() throws Exception {
        // given
        Method method = Read.class.getMethod("embeddedList");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(EmbeddedListReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedSetReadCommand() throws Exception {
        // given
        Method method = Read.class.getMethod("embeddedSet");

        // when
        ReadMapper mapper = new ReadMapper(Read.class);

        // then
        ReadCommand command = mapper.get(Read.class).get(method);
        assertThat(command).isInstanceOf(EmbeddedSetReadCommand.class);
    }

    private interface Read {

        @Id
        String id();

        @DbField("boolean")
        boolean primitiveBoolean();

        @DbField("Boolean")
        Boolean bigBoolean();

        @DbField("set")
        Set<String> set();

        @DbField("basic")
        String basic();

        @DbField("embedded")
        @DbEmbeddedDocument
        Empty embedded();

        @DbField("embeddedList")
        @DbEmbeddedDocument
        List<Empty> embeddedList();

        @DbField("embeddedSet")
        @DbEmbeddedDocument
        Set<Empty> embeddedSet();
    }
}
