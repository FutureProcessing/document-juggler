package com.futureprocessing.documentjuggler.query;

@FunctionalInterface
public interface QueryConsumer<QUERY> {

    void accept(QUERY query);
}
