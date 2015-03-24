package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Forbidden;
import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.documentjuggler.helper.Empty;
import com.futureprocessing.documentjuggler.read.command.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static com.futureprocessing.documentjuggler.Context.READ;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ReadMapperTest {

    private class NotInterface {
        @AsObjectId
        String getId() {
            return null;
        }
    }

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface() {
        // given

        try {
            // when
            ReadMapper.map(NotInterface.class);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        fail();
    }

    private interface ModelWithUnknownField {
        String getId();
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            ReadMapper.map(ModelWithUnknownField.class);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(ModelWithUnknownField.class.getMethod("getId"));
            return;
        }

        fail();
    }

    private interface Model {

        @AsObjectId
        @DbField("_id")
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

        @DbField("forbidden")
        @Forbidden(READ)
        String forbidden();
    }

    private interface ReaderWithArguments {
        @DbField("Test")
        String getTest(String arg);
    }

    @Test
    public void shouldReturnForbiddenReadCommandWhenReadMethodHasArguments() throws Exception {
        // given
        Method method = ReaderWithArguments.class.getMethod("getTest", String.class);

        // when
        ReadMapper mapper = ReadMapper.map(ReaderWithArguments.class);
        ReadCommand command = mapper.getCommand(method);

        //then
        assertThat(command).isInstanceOf(ForbiddenReadCommand.class);
    }


    @Test
    public void shouldReturnIdReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("id");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdReadCommand.class);
    }

    @Test
    public void shouldReturnBooleanReadCommandForPrimitiveBoolean() throws Exception {
        // given
        Method method = Model.class.getMethod("primitiveBoolean");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    public void shouldReturnBooleanReadCommandForBigBoolean() throws Exception {
        // given
        Method method = Model.class.getMethod("bigBoolean");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    public void shouldReturnSetReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("set");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(SetReadCommand.class);
    }

    @Test
    public void shouldReturnBasicReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("basic");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embedded");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedListReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embeddedList");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedListReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedSetReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embeddedSet");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedSetReadCommand.class);
    }

    @Test
    public void shouldReturnForbiddenReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("forbidden");

        // when
        ReadMapper mapper = ReadMapper.map(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenReadCommand.class);
    }
}
