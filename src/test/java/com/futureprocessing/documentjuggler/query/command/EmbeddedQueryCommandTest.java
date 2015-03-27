package com.futureprocessing.documentjuggler.query.command;


import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.GreaterThan;
import com.futureprocessing.documentjuggler.query.QueryMapper;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedQueryCommandTest {

    public static final String FIELD_A = "A";
    public static final String FIELD_B = "B";
    public static final String FIELD_C = "C";
    public static final String FIELD_D = "D";
    public static final String FIELD_E = "E";

    private QueryMapper mapper = QueryMapper.map(Model.class);
    private EmbeddedQueryCommand command = new EmbeddedQueryCommand(FIELD_A, new QueryProcessor<>(EmbeddedModel.class, mapper));

    private interface Model {

        @DbField(FIELD_A)
        @DbEmbeddedDocument
        Model withA(Consumer<EmbeddedModel> consumer);
    }

    private interface EmbeddedModel {

        @DbField(FIELD_B)
        EmbeddedModel withB(String value);

        @DbField(FIELD_C)
        @DbEmbeddedDocument
        EmbeddedModel withC(Consumer<DoubleEmbeddedModel> consumer);

        @DbField(FIELD_D)
        @GreaterThan
        EmbeddedModel greaterThan(int value);
    }

    private interface DoubleEmbeddedModel {

        @DbField(FIELD_E)
        DoubleEmbeddedModel withE(String value);
    }

    @Test
    public void shouldCreateQueryWithEmbeddedField() {
        // given
        String value = "testValue";

        QueryBuilder queryBuilder = new QueryBuilder();
        Consumer<EmbeddedModel> consumer = embeddedModel -> embeddedModel.withB(value);

        // when
        command.query(queryBuilder, new Object[]{consumer});

        // then
        BasicDBObject expectedQuery = new BasicDBObject(FIELD_A, new BasicDBObject(FIELD_B, value));
        assertThat(queryBuilder.get()).isEqualTo(expectedQuery);
    }

    @Test
    public void shouldCreateQueryWithDoubleEmbeddedField() {
        // given
        String value = "testValue";

        QueryBuilder queryBuilder = new QueryBuilder();
        Consumer<EmbeddedModel> consumer = embeddedModel -> embeddedModel.withC(c -> c.withE(value));

        // when
        command.query(queryBuilder, new Object[]{consumer});

        // then
        BasicDBObject expectedQuery = new BasicDBObject(FIELD_A, new BasicDBObject(FIELD_C, new BasicDBObject(FIELD_E, value)));
        assertThat(queryBuilder.get()).isEqualTo(expectedQuery);
    }

    @Test
    public void shouldCreateQueryWithEmbeddedFieldCombinedWIthAnotherAnnotation() {
        // given
        int value = 666;

        QueryBuilder queryBuilder = new QueryBuilder();
        Consumer<EmbeddedModel> consumer = embeddedModel -> embeddedModel.greaterThan(value);

        // when
        command.query(queryBuilder, new Object[]{consumer});

        // then
        BasicDBObject expectedQuery = new BasicDBObject(FIELD_A, new BasicDBObject(FIELD_D, new BasicDBObject("$gt", value)));
        assertThat(queryBuilder.get()).isEqualTo(expectedQuery);
    }
}
