package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.MappingMode;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.mongojuggler.exception.validation.UnsupportedMethodException;
import com.futureprocessing.mongojuggler.helper.Empty;
import com.futureprocessing.mongojuggler.read.command.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static com.futureprocessing.mongojuggler.MappingMode.LENIENT;
import static com.futureprocessing.mongojuggler.MappingMode.STRICT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnitParamsRunner.class)
public class ReaderMapperTest {

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface() {
        // given

        try {
            // when
            new ReaderMapper(NotInterface.class, STRICT);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        fail();
    }

    private class NotInterface {

        @Id
        String getId() {
            return null;
        }
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            new ReaderMapper(UnknownFieldQuery.class, STRICT);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(UnknownFieldQuery.class.getMethod("getId"));
            return;
        }

        fail();
    }

    private interface UnknownFieldQuery {
        String getId();
    }

    @Test
    public void shouldThrowInvalidArgumentsExceptionWhenReadMethodHasArguments() throws Exception {
        // given
        Method method = ReaderWithArguments.class.getMethod("getTest", String.class);

        try {
            // when
            new ReaderMapper(ReaderWithArguments.class, STRICT);
        } catch (UnsupportedMethodException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(method);
            return;
        }

        fail();
    }

    @Test
    public void shouldReturnUnsupportedReadCommandWhenReadMethodHasArguments() throws Exception {
        // given
        Method method = ReaderWithArguments.class.getMethod("getTest", String.class);

        // when
        ReaderMapper mapper = new ReaderMapper(ReaderWithArguments.class, LENIENT);
        ReadCommand command = mapper.getCommand(method);

        //then
        assertThat(command).isInstanceOf(UnsupportedReadCommand.class);
    }

    private interface ReaderWithArguments {
        @DbField("Test")
        String getTest(String arg);
    }

    //=====================================================

    Object[] mappingModes() {
        return MappingMode.values();
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnIdReadCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("id");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdReadCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnBooleanReadCommandForPrimitiveBoolean(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("primitiveBoolean");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnBooleanReadCommandForBigBoolean(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("bigBoolean");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanReadCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnSetReadCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("set");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(SetReadCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnBasicReadCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("basic");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicReadCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnEmbeddedReadCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("embedded");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedReadCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnEmbeddedListReadCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("embeddedList");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedListReadCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnEmbeddedSetReadCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = TestStrictModeReader.class.getMethod("embeddedSet");

        // when
        ReaderMapper mapper = new ReaderMapper(TestStrictModeReader.class, mappingMode);

        // then
        ReadCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedSetReadCommand.class);
    }

    private interface TestStrictModeReader {

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
