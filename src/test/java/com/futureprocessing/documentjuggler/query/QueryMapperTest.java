package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Id;
import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.documentjuggler.query.command.BasicQueryCommand;
import com.futureprocessing.documentjuggler.query.command.IdQueryCommand;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.futureprocessing.documentjuggler.query.command.UnsupportedQueryCommand;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class QueryMapperTest {

    private class NotInterface {
        NotInterface withId(String id) {
            return null;
        }
    }

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfQueryIsNotInterface() {
        // given

        try {
            // when
            new QueryMapper(NotInterface.class);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        fail();
    }

    private interface Model {
        @Id
        Model id(String id);

        @DbField("string")
        Model withString(String string);

        @DbField("fieldA")
        String getFieldA();
    }

    @Test
    public void shouldReturnIdQueryCommand() throws NoSuchMethodException {
        // given
        Method method = Model.class.getMethod("id", String.class);

        // when
        QueryMapper mapper = new QueryMapper(Model.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdQueryCommand.class);
    }

    @Test
    public void shouldReturnBasicQueryCommand() throws NoSuchMethodException {
        // given
        Method method = Model.class.getMethod("withString", String.class);

        // when
        QueryMapper mapper = new QueryMapper(Model.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicQueryCommand.class);
    }

    @Test
    public void shouldReturnUnsupportedMethodCommand() throws NoSuchMethodException {
        // given
        Method method = Model.class.getMethod("getFieldA");

        // when
        QueryMapper mapper = new QueryMapper(Model.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedQueryCommand.class);
    }


}
