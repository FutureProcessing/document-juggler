package com.futureprocessing.documentjuggler.assertions;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryBuilderAssert extends AbstractAssert<QueryBuilderAssert, QueryBuilder> {
    protected QueryBuilderAssert(QueryBuilder actual) {
        super(actual, QueryBuilderAssert.class);
    }

    public QueryBuilderAssert hasDocumentEqualTo(DBObject expected) {
        assertThat(actual.get()).isEqualTo(expected);
        return this;
    }
}