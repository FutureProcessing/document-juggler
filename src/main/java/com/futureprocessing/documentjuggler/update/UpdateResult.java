package com.futureprocessing.documentjuggler.update;

import com.mongodb.WriteResult;

public class UpdateResult extends OperationResult {

    public UpdateResult(WriteResult result) {
        super(result);
    }

    public void ensureOneUpdated() {
        ensureAffected(1);
    }

    public void ensureUpdated(int expected) {
        ensureAffected(expected);
    }
}
