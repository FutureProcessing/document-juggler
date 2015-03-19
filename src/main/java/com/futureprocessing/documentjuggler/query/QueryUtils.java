package com.futureprocessing.documentjuggler.query;


import static java.util.Arrays.asList;

public final class QueryUtils {

    public static <MODEL> QueryExpression<MODEL> or(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        return new QueryExpression<>(asList(consumer1, consumer2));
    }

    private QueryUtils() {}
}
