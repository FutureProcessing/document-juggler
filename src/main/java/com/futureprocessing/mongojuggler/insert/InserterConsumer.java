package com.futureprocessing.mongojuggler.insert;

@FunctionalInterface
public interface InserterConsumer<INSERTER> {

    void accept(INSERTER inserter);
}
