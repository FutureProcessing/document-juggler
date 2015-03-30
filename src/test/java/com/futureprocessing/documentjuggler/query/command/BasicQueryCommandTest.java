package com.futureprocessing.documentjuggler.query.command;

import com.futureprocessing.documentjuggler.insert.command.BasicInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicQueryCommandTest {

    private static final String FIELD = "testField";
    private static final String VALUE = "SomeValue";

    private QueryCommand command = new BasicQueryCommand(FIELD);

    @Test
    public void shouldQueryForSpecifiedValue() {
        // given
        QueryBuilder builder = QueryBuilder.start();

        // when
        command.query(builder, new Object[]{VALUE});

        // then
        DBObject query = builder.get();
        DBObject expected = new BasicDBObject(FIELD, VALUE);
        assertThat(query).isEqualTo(expected);
    }

}
