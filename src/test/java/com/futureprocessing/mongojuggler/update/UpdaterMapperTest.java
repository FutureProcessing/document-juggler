package com.futureprocessing.mongojuggler.update;


import com.futureprocessing.mongojuggler.MappingMode;
import com.futureprocessing.mongojuggler.annotation.*;
import com.futureprocessing.mongojuggler.helper.Empty;
import com.futureprocessing.mongojuggler.update.command.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.futureprocessing.mongojuggler.MappingMode.STRICT;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdaterMapperTest {

    @Test
    public void shouldReturnEmbeddedUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("embedded", Consumer.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(EmbeddedUpdateCommand.class);
    }

    @Test
    public void shouldReturnBooleanUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("bigBoolean", Boolean.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanUpdateCommand.class);
    }

    @Test
    public void shouldReturnBooleanUpdateCommandForPrimitiveBoolean() throws Exception {
        // given
        Method method = Update.class.getMethod("primitiveBoolean", boolean.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BooleanUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetSingleUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetSingle", String.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(AddToSetSingleUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetManyUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetMany", String.class, String.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(AddToSetManyUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetCollectionUpdateCommandForCollectionParam() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetCollection", Collection.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(AddToSetCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetCollectionUpdateCommandForListParam() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetList", List.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(AddToSetCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetArrayUpdateCommandForArrayParam() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetArray", String[].class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(AddToSetArrayUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetArrayUpdateCommandForVarArg() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetVarArg", String[].class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(AddToSetArrayUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushSingleUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("pushSingle", String.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushSingleUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushManyUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("pushMany", String.class, String.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushManyUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushCollectionUpdateCommandForCollectionParam() throws Exception {
        // given
        Method method = Update.class.getMethod("pushCollection", Collection.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushCollectionUpdateCommandForListParam() throws Exception {
        // given
        Method method = Update.class.getMethod("pushList", List.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushArrayUpdateCommandForArrayParam() throws Exception {
        // given
        Method method = Update.class.getMethod("pushArray", String[].class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushArrayUpdateCommand.class);
    }

    @Test
    public void shouldReturnPushArrayUpdateCommandForVarArg() throws Exception {
        // given
        Method method = Update.class.getMethod("pushVarArg", String[].class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(PushArrayUpdateCommand.class);
    }

    @Test
    public void shouldReturnIncrementUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("increment", int.class);

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IncrementUpdateCommand.class);
    }

    @Test
    public void shouldReturnUnsetUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("fieldToUnset");

        // when
        UpdaterMapper mapper = new UpdaterMapper(Update.class, STRICT);

        // then
        UpdateCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsetCommand.class);
    }


    private interface Update {

        @DbField("embedded")
        @DbEmbeddedDocument
        Update embedded(Consumer<Empty> consumer);

        @DbField("boolean")
        Update bigBoolean(Boolean value);

        @DbField("primitiveBoolean")
        Update primitiveBoolean(boolean value);

        @DbField("set")
        @AddToSet
        Update addToSetSingle(String value);

        @DbField("set")
        @AddToSet
        Update addToSetMany(String value1, String value2);

        @DbField("set")
        @AddToSet
        Update addToSetCollection(Collection<String> values);

        @DbField("set")
        @AddToSet
        Update addToSetList(List<String> values);

        @DbField("set")
        @AddToSet
        Update addToSetArray(String[] values);

        @DbField("set")
        @AddToSet
        Update addToSetVarArg(String... values);

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

        @DbField("increment")
        @Inc
        Update increment(int number);

        @DbField("fieldToUnset")
        @Unset
        Update fieldToUnset();
    }
}
