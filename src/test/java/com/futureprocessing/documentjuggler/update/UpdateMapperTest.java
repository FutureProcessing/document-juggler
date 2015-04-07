package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Forbidden;
import com.futureprocessing.documentjuggler.annotation.update.Push;
import com.futureprocessing.documentjuggler.annotation.update.Unset;
import com.futureprocessing.documentjuggler.helper.Empty;
import com.futureprocessing.documentjuggler.update.command.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.futureprocessing.documentjuggler.Context.UPDATE;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateMapperTest {

    @Test
    public void shouldReturnEmbeddedUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("embedded", Consumer.class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedUpdateCommand.class);
    }

    @Test
    public void shouldReturnBooleanUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("bigBoolean", Boolean.class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanUpdateCommand.class);
    }

    @Test
    public void shouldReturnBooleanUpdateCommandForPrimitiveBoolean() throws Exception {
        // given
        Method method = Update.class.getMethod("primitiveBoolean", boolean.class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushSingleUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("pushSingle", String.class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushSingleUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushManyUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("pushMany", String.class, String.class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushManyUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushCollectionUpdateCommandForCollectionParam() throws Exception {
        // given
        Method method = Update.class.getMethod("pushCollection", Collection.class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushCollectionUpdateCommandForListParam() throws Exception {
        // given
        Method method = Update.class.getMethod("pushList", List.class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushArrayUpdateCommandForArrayParam() throws Exception {
        // given
        Method method = Update.class.getMethod("pushArray", String[].class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushArrayUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushArrayUpdateCommandForVarArg() throws Exception {
        // given
        Method method = Update.class.getMethod("pushVarArg", String[].class);

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushArrayUpdateCommand.class);
    }

    @Test
    public void shouldReturnUnsetUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("fieldToUnset");

        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsetCommand.class);
    }

    @Test
    public void shouldReturnForbiddenUpdateCommandForGetter() throws Exception {
        // given
        Method method = Update.class.getMethod("getter");
        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenUpdateCommand.class);
    }

    @Test
    public void shouldReturnForbiddenUpdateCommandForUpdatingIDField() throws Exception {
        // given
        Method method = Update.class.getMethod("updateId", String.class);
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // when
        UpdateCommand command = mapper.get(method);

        // then
        assertThat(command).isInstanceOf(ForbiddenUpdateCommand.class);
    }

    @Test
    public void shouldReturnForbiddenUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("forbidden", String.class);
        // when
        UpdateMapper mapper = UpdateMapper.map(Update.class);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(ForbiddenUpdateCommand.class);
    }

    private interface Update {

        @DbField("embedded")
        @DbEmbeddedDocument
        Update embedded(Consumer<Empty> consumer);

        @DbField("boolean")
        Update bigBoolean(Boolean value);

        @DbField("primitiveBoolean")
        Update primitiveBoolean(boolean value);

        @DbField("list")
        @Push
        Update pushSingle(String value);

        @DbField("list")
        @Push
        Update pushMany(String value1, String value2);

        @DbField("list")
        @Push
        Update pushCollection(Collection<String> values);

        @DbField("list")
        @Push
        Update pushList(List<String> values);

        @DbField("list")
        @Push
        Update pushArray(String[] values);

        @DbField("list")
        @Push
        Update pushVarArg(String... values);

        @DbField("fieldToUnset")
        @Unset
        Update fieldToUnset();

        @DbField("getter")
        String getter();

        @DbField("forbidden")
        @Forbidden(UPDATE)
        Update forbidden(String value);

        @DbField("_id")
        Update updateId(String id);
    }
}
