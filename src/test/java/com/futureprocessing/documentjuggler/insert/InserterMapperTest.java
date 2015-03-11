package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.annotation.*;
import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.documentjuggler.helper.Empty;
import com.futureprocessing.documentjuggler.insert.command.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class InserterMapperTest {


    @Test

    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface() {
        // given

        try {
            // when
            new InserterMapper(NotInterface.class);
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
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            new InserterMapper(UnknownFieldQuery.class);
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
    public void shouldReturnEmbeddedInsertCommand() throws Exception {
        // given
        Method method = StrictModeInserter.class.getMethod("embedded", Consumer.class);

        // when
        InserterMapper mapper = new InserterMapper(StrictModeInserter.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedInsertCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedVarArgInsertCommand() throws Exception {
        // given
        Method method = StrictModeInserter.class.getMethod("embeddedVarArg", Consumer[].class);

        // when
        InserterMapper mapper = new InserterMapper(StrictModeInserter.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedVarArgInsertCommand.class);
    }

    @Test
    public void shouldReturnBasicInsertCommand() throws Exception {
        // given
        Method method = StrictModeInserter.class.getMethod("value", String.class);

        // when
        InserterMapper mapper = new InserterMapper(StrictModeInserter.class);

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
        InserterMapper mapper = new InserterMapper(UnsupportedAddToSet.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
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
        InserterMapper mapper = new InserterMapper(UnsupportedPush.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
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
        InserterMapper mapper = new InserterMapper(WrongGetter.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
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
