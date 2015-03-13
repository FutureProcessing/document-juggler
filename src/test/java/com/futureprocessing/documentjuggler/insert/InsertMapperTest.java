package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.annotation.AddToSet;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Push;
import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.documentjuggler.helper.Empty;
import com.futureprocessing.documentjuggler.insert.command.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class InsertMapperTest {


    private class NotInterface {
        @DbField("_id")
        String getId() {
            return null;
        }
    }

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfReadIsNotInterface() {
        // given

        try {
            // when
            new InsertMapper(NotInterface.class);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        Assert.fail();
    }

    private interface ModelWithUnknownQuery {
        String getId();
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            new InsertMapper(ModelWithUnknownQuery.class);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(ModelWithUnknownQuery.class.getMethod("getId"));
            return;
        }

        Assert.fail();
    }

    private interface Model {

        @DbField("embedded")
        @DbEmbeddedDocument
        Model embedded(Consumer<Empty> consumer);

        @DbField("embedded")
        @DbEmbeddedDocument
        Model embeddedVarArg(Consumer<Empty>... consumers);

        @DbField("value")
        Model value(String value);

        @DbField("set")
        @AddToSet
        Model unsupportedAddToSet(String value);

        @DbField("list")
        @Push
        Model unsupportedPush(String value1, String value2);

        @DbField("wrongGetter")
        String wrongGetter();
    }

    @Test
    public void shouldReturnEmbeddedInsertCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embedded", Consumer.class);

        // when
        InsertMapper mapper = new InsertMapper(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedInsertCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedVarArgInsertCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embeddedVarArg", Consumer[].class);

        // when
        InsertMapper mapper = new InsertMapper(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedVarArgInsertCommand.class);
    }

    @Test
    public void shouldReturnBasicInsertCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("value", String.class);

        // when
        InsertMapper mapper = new InsertMapper(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicInsertCommand.class);
    }


    @Test
    public void shouldReturnUnsupportedInsertCommandForAddToSetAnnotation() throws Exception {
        // given
        Method method = Model.class.getMethod("unsupportedAddToSet", String.class);

        // when
        InsertMapper mapper = new InsertMapper(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }

    @Test
    public void shouldReturnUnsupportedInsertCommandForPushAnnotation() throws Exception {
        // given
        Method method = Model.class.getMethod("unsupportedPush", String.class, String.class);

        // when
        InsertMapper mapper = new InsertMapper(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }

    @Test
    public void shouldReturnUnsupportedInsertCommandForIllegalMethodModel() throws Exception {
        // given
        Method method = Model.class.getMethod("wrongGetter");

        // when
        InsertMapper mapper = new InsertMapper(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedInsertCommand.class);
    }

}
