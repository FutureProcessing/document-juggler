package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Id;
import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.documentjuggler.helper.Empty;
import com.futureprocessing.documentjuggler.read.command.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ReaderMapperTest {

    private class NotInterface {
        @Id
        String getId() {
            return null;
        }
    }

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface() {
        // given

        try {
            // when
            new ReaderMapper(NotInterface.class);
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
            new ReaderMapper(ModelWithUnknownField.class);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(ModelWithUnknownField.class.getMethod("getId"));
            return;
        }

        fail();
    }

    private interface Model {

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

    private interface ReaderWithArguments {
        @DbField("Test")
        String getTest(String arg);
    }

    @Test
    public void shouldReturnUnsupportedReadCommandWhenReadMethodHasArguments() throws Exception {
        // given
        Method method = ReaderWithArguments.class.getMethod("getTest", String.class);

        // when
        ReaderMapper mapper = new ReaderMapper(ReaderWithArguments.class);
        ReadCommand command = mapper.getCommand(method);

        //then
        assertThat(command).isInstanceOf(UnsupportedReadCommand.class);
    }


    @Test
    public void shouldReturnIdReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("id");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdReadCommand.class);
    }

    @Test
    public void shouldReturnBooleanReadCommandForPrimitiveBoolean() throws Exception {
        // given
        Method method = Model.class.getMethod("primitiveBoolean");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    public void shouldReturnBooleanReadCommandForBigBoolean() throws Exception {
        // given
        Method method = Model.class.getMethod("bigBoolean");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    public void shouldReturnSetReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("set");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(SetReadCommand.class);
    }

    @Test
    public void shouldReturnBasicReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("basic");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embedded");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedListReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embeddedList");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedListReadCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedSetReadCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embeddedSet");

        // when
        ReaderMapper mapper = new ReaderMapper(Model.class);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedSetReadCommand.class);
    }
}
