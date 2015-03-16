package com.futureprocessing.documentjuggler.update;

public interface UpdateResult {

    void ensureOneUpdated();

    void ensureUpdated(int expected);

    int getAffectedCount();
}
