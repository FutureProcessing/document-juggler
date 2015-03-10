package com.futureprocessing.mongojuggler.insert;


import com.futureprocessing.mongojuggler.MappingMode;
import com.futureprocessing.mongojuggler.annotation.*;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.mongojuggler.exception.validation.UnsupportedMethodException;
import com.futureprocessing.mongojuggler.helper.Empty;
import com.futureprocessing.mongojuggler.insert.command.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(JUnitParamsRunner.class)
public class InserterMapperTest {

    Object[] mappingModes() {
        return MappingMode.values();
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface(MappingMode mappingMode) {
        // given

        try {
            // when
            new InserterMapper(NotInterface.class, mappingMode);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        Assert.fail();
    }

    private class NotInterface {
        @Id
        String getId() {
            return null;
        }
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField(MappingMode mappingMode) throws Exception {
        // given

        try {
            // when
            new InserterMapper(UnknownFieldQuery.class, mappingMode);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(UnknownFieldQuery.class.getMethod("getId"));
            return;
        }

        Assert.fail();
    }

    private interface UnknownFieldQuery {
        String getId();
    }


    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnEmbeddedInsertCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = StrictModeInserter.class.getMethod("embedded", Consumer.class);

        // when
        InserterMapper mapper = new InserterMapper(StrictModeInserter.class, mappingMode);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedInsertCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnEmbeddedVarArgInsertCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = StrictModeInserter.class.getMethod("embeddedVarArg", Consumer[].class);

        // when
        InserterMapper mapper = new InserterMapper(StrictModeInserter.class, mappingMode);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedVarArgInsertCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnBasicInsertCommand(MappingMode mappingMode) throws Exception {
        // given
        Method method = StrictModeInserter.class.getMethod("value", String.class);

        // when
        InserterMapper mapper = new InserterMapper(StrictModeInserter.class, mappingMode);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicInsertCommand.class);
    }


    private interface UnsupportedAddToSet {
        @DbField("set")
        @AddToSet
        StrictModeInserter unsupportedAddToSet(String value);
    }

    @Test
    public void shouldReturnUnsupportedInsertCommandForAddToSetAnnotationInLenientMode() throws Exception {
        // given
        Method method = UnsupportedAddToSet.class.getMethod("unsupportedAddToSet", String.class);

        // when
        InserterMapper mapper = new InserterMapper(UnsupportedAddToSet.class, MappingMode.LENIENT);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }

    @Test
    public void shouldThrowUnsupportedMethodExceptionForAddToSetAnnotationInStrictMode() throws Exception {
        // given
        Method method = UnsupportedAddToSet.class.getMethod("unsupportedAddToSet", String.class);

        // when
        try {
            new InserterMapper(UnsupportedAddToSet.class, MappingMode.STRICT);
        } catch (UnsupportedMethodException e) {
            // then
            assertThat(e.getMethod()).isEqualTo(method);
            return;
        }

        fail("Should have thrown exception");
    }

    private interface UnsupportedPush {
        @DbField("list")
        @Push
        StrictModeInserter unsupportedPush(String value1, String value2);
    }

    @Test
    public void shouldReturnUnsupportedInsertCommandForPushAnnotationInLenientMode() throws Exception {
        // given
        Method method = UnsupportedPush.class.getMethod("unsupportedPush", String.class, String.class);

        // when
        InserterMapper mapper = new InserterMapper(UnsupportedPush.class, MappingMode.LENIENT);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }

    @Test
    public void shouldThrowUnsupportedMethodExceptionForPushAnnotationInStrictMode() throws Exception {
        // given
        Method method = UnsupportedPush.class.getMethod("unsupportedPush", String.class, String.class);

        // when
        try {
            new InserterMapper(UnsupportedPush.class, MappingMode.STRICT);
        } catch (UnsupportedMethodException e) {
            // then
            assertThat(e.getMethod()).isEqualTo(method);
            return;
        }

        fail("Should have thrown exception");
    }


    private interface WrongGetter {
        @DbField("wrongGetter")
        String wrongGetter();
    }

    @Test
    public void shouldReturnUnsupportedInsertCommandForIllegalMethodInLenientMode() throws Exception {
        // given
        Method method = WrongGetter.class.getMethod("wrongGetter");

        // when
        InserterMapper mapper = new InserterMapper(WrongGetter.class, MappingMode.LENIENT);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }


    @Test
    public void shouldThrowUnsupportedMethodExceptionForIllegalMethodInStrictMode() throws Exception {
        // given
        Method method = WrongGetter.class.getMethod("wrongGetter");

        // when
        try {
            new InserterMapper(WrongGetter.class, MappingMode.STRICT);
        } catch (UnsupportedMethodException e) {
            // then
            assertThat(e.getMethod()).isEqualTo(method);
            return;
        }

        fail("Should have thrown exception");
    }

    private interface StrictModeInserter {

        @DbField("embedded")
        @DbEmbeddedDocument
        StrictModeInserter embedded(Consumer<Empty> consumer);

        @DbField("embedded")
        @DbEmbeddedDocument
        StrictModeInserter embeddedVarArg(Consumer<Empty>... consumers);

        @DbField("value")
        StrictModeInserter value(String value);
    }
}
