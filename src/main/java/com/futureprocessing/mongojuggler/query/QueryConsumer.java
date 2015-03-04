package com.futureprocessing.mongojuggler.query;

@FunctionalInterface
public interface QueryConsumer<QUERY> {

    void accept(QUERY query);
}
