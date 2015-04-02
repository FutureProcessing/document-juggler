package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Forbidden;
import com.futureprocessing.documentjuggler.annotation.update.AddToSet;
import com.futureprocessing.documentjuggler.annotation.update.Push;
import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;
import com.futureprocessing.documentjuggler.helper.Empty;
import com.futureprocessing.documentjuggler.insert.command.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static com.futureprocessing.documentjuggler.Context.INSERT;
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
            InsertMapper.map(NotInterface.class);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        Assert.fail();
    }

    private interface ModelWithUnknownQuery {
        String unknownField();
    }

    @Test
    public void shouldThrowUnknownFieldExceptionIfOneOfMethodsIsNotAnnotatedWithDbField() throws Exception {
        // given

        try {
            // when
            InsertMapper.map(ModelWithUnknownQuery.class);
        } catch (UnknownFieldException ex) {
            // then
            assertThat(ex.getMethod()).isEqualTo(ModelWithUnknownQuery.class.getMethod("unknownField"));
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

        @DbField("forbidden")
        @Forbidden(INSERT)
        Model forbidden(String test);
    }

    @Test
    public void shouldReturnEmbeddedInsertCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embedded", Consumer.class);

        // when
        InsertMapper mapper = InsertMapper.map(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedInsertCommand.class);
    }

    @Test
    public void shouldReturnEmbeddedVarArgInsertCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("embeddedVarArg", Consumer[].class);

        // when
        InsertMapper mapper = InsertMapper.map(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedVarArgInsertCommand.class);
    }

    @Test
    public void shouldReturnBasicInsertCommand() throws Exception {
        // given
        Method method = Model.class.getMethod("value", String.class);

        // when
        InsertMapper mapper = InsertMapper.map(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicInsertCommand.class);
    }


    @Test
    public void shouldReturnForbiddenInsertCommandForAddToSetAnnotation() throws Exception {
        // given
        Method method = Model.class.getMethod("unsupportedAddToSet", String.class);

        // when
        InsertMapper mapper = InsertMapper.map(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenInsertCommand.class);
    }

    @Test
    public void shouldReturnForbiddenInsertCommandForPushAnnotation() throws Exception {
        // given
        Method method = Model.class.getMethod("unsupportedPush", String.class, String.class);

        // when
        InsertMapper mapper = InsertMapper.map(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenInsertCommand.class);
    }

    @Test
    public void shouldReturnForbiddenInsertCommandForIllegalMethodModel() throws Exception {
        // given
        Method method = Model.class.getMethod("wrongGetter");

        // when
        InsertMapper mapper = InsertMapper.map(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenInsertCommand.class);
    }

    @Test
    public void shouldReturnForbiddenInsertCommandForForbiddenMethodModel() throws Exception {
        // given
        Method method = Model.class.getMethod("forbidden", String.class);

        // when
        InsertMapper mapper = InsertMapper.map(Model.class);

        // then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenInsertCommand.class);
    }

    interface ModelWithIdAsObjectId {
        @AsObjectId
        @DbField("_id")
        ModelWithIdAsObjectId withId(String id);
    }

    @Test
    public void shouldReturnIdInsertCommand() throws NoSuchMethodException {
        //given
        Method method = ModelWithIdAsObjectId.class.getMethod("withId", String.class);

        //when
        InsertMapper mapper = InsertMapper.map(ModelWithIdAsObjectId.class);

        //then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdInsertCommand.class);
    }

    interface ModelWithWrongIdMapping {
        @AsObjectId
        @DbField("_id")
        ModelWithIdAsObjectId withId(Double id);
    }

    @Test
    public void shouldReturnForbiddenInsertCommandForWrongIdMapping() throws NoSuchMethodException {
        //given
        Method method = ModelWithWrongIdMapping.class.getMethod("withId", Double.class);

        //when
        InsertMapper mapper = InsertMapper.map(ModelWithWrongIdMapping.class);

        //then
        InsertCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenInsertCommand.class);
    }

}
