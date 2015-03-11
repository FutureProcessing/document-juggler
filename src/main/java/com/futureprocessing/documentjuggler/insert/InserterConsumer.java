package com.futureprocessing.documentjuggler.insert;

@FunctionalInterface
public interface InserterConsumer<INSERTER> {

    void accept(INSERTER inserter);
}
