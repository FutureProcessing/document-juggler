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

public class QuerierMapperTest {

    @Test
    public void shouldThrowModelIsNotInterfaceExceptionIfQueryIsNotInterface() {
        // given

        try {
            // when
            new QuerierMapper(NotInterface.class);
        } catch (ModelIsNotInterfaceException ex) {
            //then
            assertThat(ex.getClazz()).isEqualTo(NotInterface.class);
            return;
        }

        fail();
    }

    private class NotInterface {

        NotInterface withId(String id) {
            return null;
        }
    }

    @Test
    public void shouldReturnIdQueryCommand() throws NoSuchMethodException {
        // given
        Method method = TestStrictModeQuerier.class.getMethod("id", String.class);

        // when
        QuerierMapper mapper = new QuerierMapper(TestStrictModeQuerier.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdQueryCommand.class);
    }

    @Test
    public void shouldReturnBasicQueryCommand() throws NoSuchMethodException {
        // given
        Method method = TestStrictModeQuerier.class.getMethod("withString", String.class);

        // when
        QuerierMapper mapper = new QuerierMapper(TestStrictModeQuerier.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(BasicQueryCommand.class);
    }

    private interface TestStrictModeQuerier {

        @Id
        TestStrictModeQuerier id(String id);

        @DbField("string")
        TestStrictModeQuerier withString(String string);
    }

    private interface TestEasyModeQuerier {
        @DbField("fieldA")
        String getFieldA();
    }

    @Test
    public void shouldReturnUnsupportedMethodCommand() throws NoSuchMethodException {
        // given
        Method method = TestEasyModeQuerier.class.getMethod("getFieldA");

        // when
        QuerierMapper mapper = new QuerierMapper(TestEasyModeQuerier.class);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedQueryCommand.class);
    }


}
