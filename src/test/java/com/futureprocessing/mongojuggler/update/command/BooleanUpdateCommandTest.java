package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.update.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.update.UpdateBuilder;
import com.mongodb.BasicDBObject;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class BooleanUpdateCommandTest {

    private static final String FIELD = "testField";

    public Object[] parameters() {
        return $(
                $(false, false, false, "$set", false),
                $(false, false, true, "$set", true),
                $(false, false, null, "$set", null),
                $(true, false, false, "$set", false),
                $(true, false, true, "$set", true),
                $(true, false, null, "$unset", null),
                $(false, true, false, "$unset", null),
                $(false, true, true, "$set", true),
                $(false, true, null, "$set", null),
                $(true, true, false, "$unset", null),
                $(true, true, true, "$set", true),
                $(true, true, null, "$unset", null)
        );
    }

    @Test
    @Parameters(method = "parameters")
    public void testAll(boolean unsetIfNull, boolean unsetIfFalse, Boolean valueToSet, String expectedOperation, Boolean expectedSetValue) {
        // given
        UpdateCommand command = new BooleanUpdateCommand(FIELD, unsetIfNull, unsetIfFalse);
        UpdateBuilder builder = new RootUpdateBuilder();

        // when
        command.update(builder, new Object[]{valueToSet});

        // then
        BasicDBObject expected = new BasicDBObject(expectedOperation, new BasicDBObject(FIELD, expectedSetValue));
        assertThat(builder.getDocument()).isEqualTo(expected);
    }
}
