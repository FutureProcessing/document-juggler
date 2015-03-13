package com.futureprocessing.documentjuggler.update;

@FunctionalInterface
public interface UpdatConsumer<UPDATER> {

    void accept(UPDATER updater);
}
