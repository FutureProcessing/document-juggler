package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.query.expression.OrQueryExpression;

import static java.util.Arrays.asList;

public final class QueryUtils {

    public static <MODEL> OrQueryExpression<MODEL> or(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        return new OrQueryExpression<>(asList(consumer1, consumer2));
    }

    private QueryUtils() {
    }
}
