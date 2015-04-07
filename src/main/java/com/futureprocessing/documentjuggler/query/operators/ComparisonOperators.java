package com.futureprocessing.documentjuggler.query.operators;

import com.futureprocessing.documentjuggler.query.command.*;
import com.mongodb.QueryBuilder;

import java.util.Collection;

public class ComparisonOperators<TYPE> {

    private final String field;
    private final QueryBuilder builder;

    public ComparisonOperators(String field, QueryBuilder builder) {
        this.field = field;
        this.builder = builder;
    }

    public ComparisonOperators<TYPE> greaterThan(TYPE value) {
        GreaterThanQueryCommand.query(field, builder, value);
        return this;
    }

    public ComparisonOperators<TYPE> greaterThanEquals(TYPE value) {
        GreaterThanEqualsQueryCommand.query(field, builder, value);
        return this;
    }

    public ComparisonOperators<TYPE> lessThan(TYPE value) {
        LessThanQueryCommand.query(field, builder, value);
        return this;
    }

    public ComparisonOperators<TYPE> lessThanEquals(TYPE value) {
        LessThanEqualsQueryCommand.query(field, builder, value);
        return this;
    }

    public ComparisonOperators<TYPE> in(Collection<TYPE> collection) {
        builder.and(field).in(collection);
        return this;
    }

    public ComparisonOperators<TYPE> in(int[] args) {
        builder.and(field).in(args);
        return this;
    }

    public ComparisonOperators<TYPE> in(TYPE... args) {
        builder.and(field).in(args);
        return this;
    }

    public ComparisonOperators<TYPE> notIn(Collection<TYPE> collection) {
        builder.and(field).notIn(collection);
        return this;
    }

    public ComparisonOperators<TYPE> notIn(int[] args) {
        builder.and(field).notIn(args);
        return this;
    }

    public ComparisonOperators<TYPE> notIn(TYPE... args) {
        builder.and(field).notIn(args);
        return this;
    }

    public ComparisonOperators<TYPE> notEquals(TYPE value) {
        NotEqualsQueryCommand.query(field, builder, value);
        return this;
    }
}
