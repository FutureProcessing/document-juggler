package com.futureprocessing.mongojuggler.query;

@FunctionalInterface
public interface QuerierConsumer<QUERY> {

    void accept(QUERY query);
}
