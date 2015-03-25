package com.futureprocessing.documentjuggler.update;

@FunctionalInterface
public interface UpdateConsumer<UPDATER> {

    void accept(UPDATER updater);
}
