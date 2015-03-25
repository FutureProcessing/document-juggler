package com.futureprocessing.documentjuggler.insert;

@FunctionalInterface
public interface InsertConsumer<INSERTER> {

    void accept(INSERTER inserter);
}
