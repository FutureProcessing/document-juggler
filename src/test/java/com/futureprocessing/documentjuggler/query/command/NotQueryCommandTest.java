package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotQueryCommandTest {

    private static final String FIELD = "testField";
    private static final DBObject VALUE = new BasicDBObject("$gt", "SomeValue");


    @Test
    public void shouldQueryForSpecifiedValue() {
        // given
        QueryCommand command = new BasicQueryCommand(FIELD);
        QueryCommand notCommand = new NotQueryCommand(FIELD, command);
        QueryBuilder builder = QueryBuilder.start();

        // when
        notCommand.query(builder, new Object[]{VALUE});

        // then
        DBObject query = builder.get();
        DBObject expected = new BasicDBObject(FIELD, new BasicDBObject("$not", VALUE));
        assertThat(query).isEqualTo(expected);
    }

}
