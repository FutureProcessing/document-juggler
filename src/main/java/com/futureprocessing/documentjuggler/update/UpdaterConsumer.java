package com.futureprocessing.documentjuggler.update;

@FunctionalInterface
public interface UpdaterConsumer<UPDATER> {

    void accept(UPDATER updater);
}
