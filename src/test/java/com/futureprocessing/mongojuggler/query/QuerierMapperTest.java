package com.futureprocessing.mongojuggler.query;

import com.futureprocessing.mongojuggler.MappingMode;
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

import static com.futureprocessing.mongojuggler.MappingMode.LENIENT;
import static com.futureprocessing.mongojuggler.MappingMode.STRICT;
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
            new QuerierMapper(NotInterface.class, STRICT);
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

    Object[] mappingModes(){
        return MappingMode.values();
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnIdQueryCommand(MappingMode mappingMode) throws NoSuchMethodException {
        // given
        Method method = TestStrictModeQuerier.class.getMethod("id", String.class);

        // when
        QuerierMapper mapper = new QuerierMapper(TestStrictModeQuerier.class, mappingMode);

        // then
        QueryCommand command = mapper.get(method);
        assertThat(command).isInstanceOf(IdQueryCommand.class);
    }

    @Test
    @Parameters(method = "mappingModes")
    public void shouldReturnBasicQueryCommand(MappingMode mappingMode) throws NoSuchMethodException {
        // given
        Method method = TestStrictModeQuerier.class.getMethod("withString", String.class);

        // when
        QuerierMapper mapper = new QuerierMapper(TestStrictModeQuerier.class, mappingMode);

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
        QuerierMapper mapper = new QuerierMapper(TestEasyModeQuerier.class, LENIENT);

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
            new QuerierMapper(TestEasyModeQuerier.class, STRICT);
        } catch (UnsupportedMethodException e) {
            //then
            assertThat(e.getMethod()).isEqualTo(method);
            return;
        }

        fail("Should have thrown exception");
    }

}
