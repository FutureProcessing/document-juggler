package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryProcessorTest {

    private QueryProcessor<Model> queryProcessor;

    interface Model {
        @DbField("field")
        Model withField(String field);
    }

    @Before
    public void createQueryProcessor() {
        queryProcessor = new QueryProcessor<>(Model.class);
    }

    @Test
    public void shouldReturnNullForAbsentConsumer() {
        //given

        //when
        DBObject query = queryProcessor.process((Consumer<Model>) null);

        //then
        assertThat(query).isNull();
    }

    @Test
    public void shouldProcessModel() {
        //given

        //when
        DBObject query = queryProcessor.process(m -> m.withField("VALUE"));

        //then
        assertThat(query.get("field")).isEqualTo("VALUE");
    }
}
