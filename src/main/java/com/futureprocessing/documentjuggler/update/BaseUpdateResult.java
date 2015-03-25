package com.futureprocessing.documentjuggler.update;

import com.mongodb.WriteResult;

public class BaseUpdateResult extends OperationResult implements UpdateResult {

    public BaseUpdateResult(WriteResult result) {
        super(result);
    }

    public void ensureOneUpdated() {
        ensureAffected(1);
    }

    public void ensureUpdated(int expected) {
        ensureAffected(expected);
    }
}
