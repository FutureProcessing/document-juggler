package com.futureprocessing.mongojuggler.update;

@FunctionalInterface
public interface UpdaterConsumer<UPDATER> {

    void accept(UPDATER updater);
}
