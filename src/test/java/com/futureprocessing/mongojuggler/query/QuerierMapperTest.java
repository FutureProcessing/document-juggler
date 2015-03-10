package com.futureprocessing.mongojuggler.query;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.exception.validation.*;
import com.futureprocessing.mongojuggler.query.command.BasicQueryCommand;
import com.futureprocessing.mongojuggler.query.command.IdQueryCommand;
import com.futureprocessing.mongojuggler.query.command.QueryCommand;
import com.futureprocessing.mongojuggler.query.command.UnsupportedQueryCommand;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnitParamsRunner.class)
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

    Object[] trueFalseParameters(){
        return $(true, false);
    }

    @Test
    @Parameters(method = "trueFalseParameters")
    public void shouldReturnIdQueryCommand(boolean isStrictMode) throws NoSuchMethodException {
        // given
        Method method = TestStrictModeQuerier.class.getMethod("id", String.class);

        // when
        QuerierMapper mapper = new QuerierMapper(TestStrictModeQuerier.class, isStrictMode);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdQueryCommand.class);
    }

    @Test
    @Parameters(method = "trueFalseParameters")
    public void shouldReturnBasicQueryCommand(boolean isStrictMode) throws NoSuchMethodException {
        // given
        Method method = TestStrictModeQuerier.class.getMethod("withString", String.class);

        // when
        QuerierMapper mapper = new QuerierMapper(TestStrictModeQuerier.class, isStrictMode);

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
        QuerierMapper mapper = new QuerierMapper(TestEasyModeQuerier.class, false);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(UnsupportedQueryCommand.class);
    }

    @Test
    public void shouldThrowIllegalMethodException() throws NoSuchMethodException {
        // given
        Method method = TestEasyModeQuerier.class.getMethod("getFieldA");

        try {
            // when
            new QuerierMapper(TestEasyModeQuerier.class, true);
        } catch (UnsupportedMethodException e) {
            //then
            assertThat(e.getMethod()).isEqualTo(method);
            return;
        }

        fail("Should have thrown exception");
    }

}
