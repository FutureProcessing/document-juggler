package com.futureprocessing.documentjuggler.query;

@FunctionalInterface
public interface QuerierConsumer<QUERY> {

    void accept(QUERY query);
}
