package com.futureprocessing.mongojuggler.update;


import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.helper.Empty;
import com.futureprocessing.mongojuggler.update.command.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateMapperTest {

    @Test
    public void shouldReturnEmbeddedUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("embedded", Consumer.class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(EmbeddedUpdateCommand.class);
    }

    @Test
    public void shouldReturnBooleanUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("bigBoolean", Boolean.class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(BooleanUpdateCommand.class);
    }

    @Test
    public void shouldReturnBooleanUpdateCommandForPrimitiveBoolean() throws Exception {
        // given
        Method method = Update.class.getMethod("primitiveBoolean", boolean.class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(BooleanUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetSingleUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetSingle", String.class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(AddToSetSingleUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetManyUpdateCommand() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetMany", String.class, String.class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(AddToSetManyUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetCollectionUpdateCommandForCollectionParam() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetCollection", Collection.class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(AddToSetCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetCollectionUpdateCommandForListParam() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetList", List.class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(AddToSetCollectionUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetArrayUpdateCommandForArrayParam() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetArray", String[].class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(AddToSetArrayUpdateCommand.class);
    }

    @Test
    public void shouldReturnAddToSetArrayUpdateCommandForVarArg() throws Exception {
        // given
        Method method = Update.class.getMethod("addToSetVarArg", String[].class);

        // when
        UpdateMapper mapper = new UpdateMapper(Update.class);

        // then
        UpdateCommand command = mapper.get(Update.class).get(method);
        assertThat(command).isInstanceOf(AddToSetArrayUpdateCommand.class);
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
    }
}
